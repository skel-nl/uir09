package skel.guituner;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import skel.android.audio.AudioReader;
import skel.android.util.Logger;
import skel.misc.note.Note;
import skel.misc.note.Tuning;
import skel.misc.spectrum.SpectrumAnalyzer;

public class Tuner extends Activity {

    private static final Logger logger = Logger.getLogger(Tuner.class);

    private static final String NOTE_N_KEY = "note_n";
    private static final String NOTE_OCTAVE_KEY = "note_octave";
    private static final int SHIFT = 3;

    private final View.OnClickListener onClickListener = new TunerOnClickListener();
    private final Handler handler = new TunerHandler();

    private final Button[] stringButtons = new Button[6];

    private TextView curNoteTextView;
    private TextView curFreqTextView;
    private TextView flatTextView;
    private TextView sharpTextView;

    private AudioReader audioReader;
    private SpectrumAnalyzer spectrumAnalyzer;

    private int sampleRate;
    private int sampleLog;

    private Tuning tuning;
    private Note note;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.tuner_page);

        findViewById(R.id.note_down_id).setOnClickListener(onClickListener);
        findViewById(R.id.note_up_id).setOnClickListener(onClickListener);

        // text on these buttons will be updated
        stringButtons[0] = (Button) findViewById(R.id.string_1);
        stringButtons[1] = (Button) findViewById(R.id.string_2);
        stringButtons[2] = (Button) findViewById(R.id.string_3);
        stringButtons[3] = (Button) findViewById(R.id.string_4);
        stringButtons[4] = (Button) findViewById(R.id.string_5);
        stringButtons[5] = (Button) findViewById(R.id.string_6);

        for (Button stringButton : stringButtons) {
            stringButton.setOnClickListener(onClickListener);
        }

        curNoteTextView = (TextView) findViewById(R.id.note_id);
        curFreqTextView = (TextView) findViewById(R.id.cur_freq_id);
        flatTextView = (TextView) findViewById(R.id.flat_id);
        sharpTextView = (TextView) findViewById(R.id.sharp_id);
    }

    @Override
    protected void onResume() {
        super.onResume();

        tuning = Prefs.getTuning(this);
        sampleRate = Prefs.getSampleRate(this);
        sampleLog = Prefs.getSampleLog(this);

        // update buttons
        for (int i = 0; i < stringButtons.length; i++) {
            stringButtons[i].setText(tuning.getNote(i).toString());
        }

        // load the last used note
        SharedPreferences preferences = getPreferences(MODE_PRIVATE);

        Note defaultNote = tuning.getNote(0);
        int noteN = preferences.getInt(NOTE_N_KEY, defaultNote.getNote().ordinal());
        int octave = preferences.getInt(NOTE_OCTAVE_KEY, defaultNote.getOctave());

        updateNote(new Note(noteN, octave));

        startReader();
    }

    @Override
    protected void onPause() {
        super.onPause();
        stopReader();
        clearColors();

        // save the last used note
        SharedPreferences preferences = getPreferences(MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();

        editor.putInt(NOTE_N_KEY, note.getNote().ordinal());
        editor.putInt(NOTE_OCTAVE_KEY, note.getOctave());

        editor.commit();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        getMenuInflater().inflate(R.menu.menu, menu);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_prefs_id:
                startActivity(new Intent(this, Prefs.class));
                return true;
        }

        return false;
    }

    private void startReader() {
        if (audioReader == null || audioReader.isStopped()) {
            double minFreq = note.shift(-SHIFT).getFrequency();
            double maxFreq = note.shift(SHIFT).getFrequency();

            spectrumAnalyzer = new SpectrumAnalyzer(sampleRate, sampleLog, minFreq, maxFreq);
            audioReader = new AudioReader(sampleRate, sampleLog, handler);

            audioReader.start();
        }
    }

    private void stopReader() {
        if (audioReader != null && audioReader.isStarted()) {
            audioReader.stop();
            audioReader = null;
        }
    }

    private void updateFrequency(double frequency) {
        curFreqTextView.setText(String.format("%.02f", frequency));
    }

    private void updateNote(Note newNote) {
        note = newNote;

        curNoteTextView.setText(note.toString());
        updateFrequency(note.getFrequency());

        clearColors();
    }

    private void updateColor(TextView textView) {
        textView.setTextColor(Color.YELLOW);
    }

    private void clearColors() {
        curNoteTextView.setTextColor(Color.WHITE);
        flatTextView.setTextColor(Color.WHITE);
        sharpTextView.setTextColor(Color.WHITE);
    }

    private class TunerOnClickListener implements View.OnClickListener {

        @Override
        public void onClick(View v) {
            stopReader();

            switch (v.getId()) {
                // string buttons
                case R.id.string_1:
                    updateNote(tuning.getNote(0));
                    break;
                case R.id.string_2:
                    updateNote(tuning.getNote(1));
                    break;
                case R.id.string_3:
                    updateNote(tuning.getNote(2));
                    break;
                case R.id.string_4:
                    updateNote(tuning.getNote(3));
                    break;
                case R.id.string_5:
                    updateNote(tuning.getNote(4));
                    break;
                case R.id.string_6:
                    updateNote(tuning.getNote(5));
                    break;

                // note buttons
                case R.id.note_down_id:
                    updateNote(note.shift(-1));
                    break;
                case R.id.note_up_id:
                    updateNote(note.shift(1));
                    break;
            }

            startReader();
        }
    }

    private class TunerHandler extends Handler {

        @Override
        public void handleMessage(Message msg) {
            double[] buffer = cast(msg.obj);
            logger.v("Data was received");

            double frequency = spectrumAnalyzer.getFrequency(buffer);
            logger.d("Frequency: " + frequency);

            if (frequency > 0) {
                updateFrequency(frequency);

                clearColors();
                if (note.frequencyIsEqual(frequency, spectrumAnalyzer.getStep() / 2)) {
                    updateColor(curNoteTextView);
                } else if (note.getFrequency() > frequency) {
                    updateColor(flatTextView);
                } else {
                    updateColor(sharpTextView);
                }
            }
        }

        private double[] cast(Object obj) {
            short[] ar = (short[]) obj;
            double[] res = new double[ar.length];

            for (int i = 0; i < ar.length; i++) {
                res[i] = ar[i];
            }

            return res;
        }
    }
}
