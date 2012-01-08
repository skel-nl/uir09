package skel.misc.note;

import java.util.HashMap;
import java.util.Map;

/**
 * @author Alexey Dudarev
 */
public class Note implements Comparable<Note> {

    private static final double E = 0.1;

    private static final Note LA_4 = new Note(OctaveNote.LA, 4);
    private static final double LA_4_FREQ = 440.00;

    private static final Map<Note, Double> frequenciesMap = new HashMap<Note, Double>() {{
        put(LA_4, LA_4_FREQ);
    }};


    private final OctaveNote note;
    private final int octave;

    public Note(int note, int octave) {
        this(OctaveNote.R.resolve(note), octave);
    }

    public Note(OctaveNote note, int octave) {
        this.note = note;
        this.octave = octave;
    }

    private int distanceTo(Note anotherNote) {
        return 12 * (octave - anotherNote.octave) + note.ordinal() - anotherNote.note.ordinal();
    }

    public double getFrequency() {
        if (frequenciesMap.containsKey(this)) {
            return frequenciesMap.get(this);
        } else {
            int distance = distanceTo(LA_4);
            double frequency = LA_4_FREQ * Math.pow(2, distance / 12.0);

            frequenciesMap.put(this, frequency);
            return frequency;
        }
    }

    public boolean frequencyIsEqual(double frequency) {
        return frequencyIsEqual(frequency, E);
    }

    public boolean frequencyIsEqual(double frequency, double er) {
        return Math.abs(getFrequency() - frequency) < er;
    }

    public Note shift(int shift) {
        int newNote = note.ordinal() + shift % 12;
        int newOctave = octave + shift / 12;

        int sign = newNote < 0 ? -1 : newNote > 11 ? 1 : 0; // new octave or not
        newNote -= 12 * sign;
        newOctave += sign;

        return new Note(newNote, newOctave);
    }

    @Override
    public int compareTo(Note anotherNote) {
        return distanceTo(anotherNote);   // distance > 0 when current note is higher and vice versa
    }

    public OctaveNote getNote() {
        return note;
    }

    public int getOctave() {
        return octave;
    }

    @Override
    public String toString() {
        return note.getLabel() + String.valueOf(octave);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }

        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        Note note = (Note) o;

        return this.note == note.note && this.octave == note.octave;
    }

    @Override
    public int hashCode() {
        int result = note.hashCode();
        result = 31 * result + octave;

        return result;
    }
}
