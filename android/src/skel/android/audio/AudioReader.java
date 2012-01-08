package skel.android.audio;

import android.media.AudioFormat;
import android.media.AudioRecord;
import android.media.MediaRecorder;
import android.os.Handler;
import android.os.Message;

import skel.android.util.Logger;
import skel.misc.util.Validate;

/**
 * @author Alexey Dudarev
 */
public class AudioReader {

    private static final Logger logger = Logger.getLogger(AudioReader.class);

    private static final int READY = 0;
    private static final int STARTED = 1;
    private static final int STOPPED = 2;

    private final int sampleSize;

    private final AudioRecord audioRecord;

    private final Thread readerThread;
    private int state;

    public AudioReader(int sampleRate, int sampleLog, Handler handler) {
        Validate.isTrue(sampleRate > 0, "Invalid sampleRate: " + sampleRate);
        Validate.isTrue(sampleLog > 0, "Invalid sampleLog: " + sampleLog);
        Validate.notNull(handler);

        int bufSize = AudioRecord.getMinBufferSize(sampleRate,
                AudioFormat.CHANNEL_IN_MONO, AudioFormat.ENCODING_PCM_16BIT);

        logger.d("Buffer size: " + bufSize);

        logger.v("Initialize AudioRecord");
        audioRecord = new AudioRecord(MediaRecorder.AudioSource.MIC, sampleRate,
                AudioFormat.CHANNEL_IN_MONO, AudioFormat.ENCODING_PCM_16BIT, bufSize);

        sampleSize = 1 << sampleLog;    // 2 ^ sampleLog
        readerThread = new ReaderThread(handler);

        setState(READY);
    }

    public void start() {
        Validate.isTrue(isReady(), "AudioReader is not ready");

        if (audioRecord.getState() != AudioRecord.STATE_INITIALIZED) {
            logger.e("Failed to initialize AudioRecord");
            setState(STOPPED);

            return;
        }

        logger.v("Start ReaderThread");

        setState(STARTED);
        readerThread.start();
    }

    public void stop() {
        Validate.isTrue(isStarted(), "AudioReader is not started");

        setState(STOPPED);
        try {
            readerThread.join();
            logger.v("Stop ReaderThread");
        } catch (InterruptedException e) {
            logger.w(e);
        }

        audioRecord.release();

        logger.v("AudioRecord released");
    }

    private class ReaderThread extends Thread {

        private final Handler handler;

        public ReaderThread(Handler handler) {
            Validate.notNull(handler);
            this.handler = handler;
        }

        @Override
        public void run() {
            try {
                logger.i("Start recording");
                audioRecord.startRecording();

                short[] buffer = new short[sampleSize];
                int offset = 0;

                while (isStarted()) {
                    int n = audioRecord.read(buffer, offset, sampleSize - offset);
                    logger.d("AudioRecord read " + n + " of shorts");

                    if (n < 0) {
                        logger.e("Failed to read from AudioRecord with error code " + n);
                        setState(STOPPED);

                        return;
                    }

                    if (n == 0) {
                        logger.w("AudioRecord returned no data");
                    }

                    int end = offset + n;
                    logger.d("Buffer's size is " + end);

                    if (end >= sampleSize) {
                        offset = 0;
                        logger.d("Buffer is full, send it to listener");
                        Message msg = new Message();
                        msg.obj = buffer;
                        handler.sendMessage(msg);
                    } else {
                        offset = end;
                    }
                }
            } finally {
                if (audioRecord.getState() == AudioRecord.RECORDSTATE_RECORDING) {
                    audioRecord.stop();
                }
                logger.i("Recording stopped");
            }
        }
    }

    private synchronized void setState(int state) {
        this.state = state;
    }

    public synchronized boolean isReady() {
        return state == READY;
    }

    public synchronized boolean isStarted() {
        return state == STARTED;
    }

    public synchronized boolean isStopped() {
        return state == STOPPED;
    }
}
