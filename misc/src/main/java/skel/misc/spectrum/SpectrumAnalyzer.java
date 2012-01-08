package skel.misc.spectrum;

import skel.misc.util.Validate;

/**
 * @author Alexey Dudarev
 */
public class SpectrumAnalyzer {

    private static final double THRESHOLD = 0.3;

    private final Fft fft;
    private final double step;
    private final double minFrequency;
    private final double maxFrequency;

    public SpectrumAnalyzer(int sampleRate, int sampleLog, double minFrequency,
            double maxFrequency)
    {
        Validate.isTrue(sampleRate > 0, "Invalid sample rate: " + sampleRate);
        Validate.isTrue(sampleLog > 0, "Invalid sample log: " + sampleLog);

        Validate.isTrue(minFrequency >= 0,
                "Invalid minimal frequency: " + minFrequency);
        Validate.isTrue(maxFrequency >= minFrequency && maxFrequency <= sampleRate / 2.0,
                "Invalid maximal frequency: " + minFrequency);

        fft = new Fft(sampleLog);
        step = 1.0 * sampleRate / (1 << sampleLog);

        this.minFrequency = minFrequency;
        this.maxFrequency = maxFrequency;
    }

    public double getFrequency(double[] sample) {
        int n = sample.length;

        double xr[] = new double[n];
        double xi[] = new double[n];
        for (int i = 0; i < n; i++) {
            xr[i] = sample[i];
            xi[i] = 0;
        }

        fft.doFFT(xr, xi, false);

        
        double[] amplitudes = new double[n];
        double maxAmplitude = 0.0;
        
        for (int i = 0; i < n; i++) {
            double curAmplitude = Math.hypot(xr[i], xi[i]);
            amplitudes[i] = curAmplitude;

            if (curAmplitude > maxAmplitude) {
                maxAmplitude = curAmplitude;
            }
        }


        double frequency = -1.0;
        double amplitude = THRESHOLD;

        for (int i = (int) (minFrequency / step); i < (int) (maxFrequency / step); i++) {
            double curAmplitude = amplitudes[i] / maxAmplitude;

            if (curAmplitude > amplitude) {
                amplitude = curAmplitude;
                frequency = i * step;
            }
        }

        return frequency;
    }

    public double getStep() {
        return step;
    }
}
