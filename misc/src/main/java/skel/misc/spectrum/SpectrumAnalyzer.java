package skel.misc.spectrum;

import skel.misc.util.Validate;

/**
 * @author Alexey Dudarev
 */
public class SpectrumAnalyzer {

    private final Fft fft;
    private final double step;
    private final int minIndex;
    private final int maxIndex;

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

        minIndex = (int) (minFrequency / step);
        maxIndex = (int) (maxFrequency / step) + 1;
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

        int index = -1;
        double amplitude = 0;

        for (int i = minIndex; i < maxIndex; i++) {
            double curAmplitude = Math.hypot(xr[i], xi[i]);

            if (curAmplitude > amplitude) {
                amplitude = curAmplitude;
                index = i;
            }
        }

        return index * step;
    }

    public double getStep() {
        return step;
    }
}
