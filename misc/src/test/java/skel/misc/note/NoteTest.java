package skel.misc.note;

import org.junit.Assert;
import org.junit.Test;

/**
 * @author Alexey Dudarev
 */
public class NoteTest {

    @Test
    public void testShift() {
        Note c1 = new Note(OctaveNote.DO, 1);
        Note c2 = new Note(OctaveNote.DO, 2);
        Note g1 = new Note(OctaveNote.SOL, 1);
        Note g2 = new Note(OctaveNote.SOL, 2);

        Assert.assertEquals(c1.shift(12).shift(-12), c1);
        Assert.assertEquals(c1.shift(12), c2);
        Assert.assertEquals(c1.shift(24), c2.shift(12));
        Assert.assertEquals(g1, c1.shift(7));
        Assert.assertEquals(c1.shift(7), c2.shift(-5));
        Assert.assertEquals(c1.shift(19), g2);
    }

    @Test
    public void testGetFrequency() {
        Note a3 = new Note(OctaveNote.LA, 3);
        Note a4 = new Note(OctaveNote.LA, 4);
        Note a5 = new Note(OctaveNote.LA, 5);
        Note e4 = new Note(OctaveNote.MI, 4);
        Note g2 = new Note(OctaveNote.SOL, 2);
        Note f7 = new Note(OctaveNote.FA, 7);

        Assert.assertTrue(a3.frequencyIsEqual(220.00));
        Assert.assertTrue(a4.frequencyIsEqual(440.00));
        Assert.assertTrue(a5.frequencyIsEqual(880.00));
        Assert.assertTrue(e4.frequencyIsEqual(329.63));
        Assert.assertTrue(g2.frequencyIsEqual(98.00));
        Assert.assertTrue(f7.frequencyIsEqual(2793.83));
    }
}
