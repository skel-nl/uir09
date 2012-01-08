package skel.misc.spectrum;

import org.junit.Assert;
import org.junit.Test;

/**
 * @author Alexey Dudarev
 */
public class SpectrumAnalyzerTest {

    @Test
    public void testGetFrequency() throws Exception {

        int sampleRate = 8000;
        int sampleLog = 13;
        int sampleSize = 1 << sampleLog;
        double theorFreq = 10.0;

        double[] sample = new double[sampleSize];
        for (int i = 0; i < sampleSize; i++) {
            sample[i] = Math.sin(2 * Math.PI * i * theorFreq / sampleRate);
        }

        SpectrumAnalyzer spectrumAnalyzer =
                new SpectrumAnalyzer(sampleRate, sampleLog, 0.0, sampleRate / 2.0);

        double practFreq = spectrumAnalyzer.getFrequency(sample);
        Assert.assertTrue(Math.abs(theorFreq - practFreq) < spectrumAnalyzer.getStep() / 2.0);
    }
}
