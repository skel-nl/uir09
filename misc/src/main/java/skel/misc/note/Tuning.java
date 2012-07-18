package skel.misc.note;

/**
 * @author Alexey Dudarev
 */
public class Tuning {

    private final Note[] notes;

    public Tuning(Note... strings) {
        this.notes = strings;
    }

    public Note getNote(int stringN) {
        return notes[stringN];
    }

    public Tuning shift(int shift) {
        Note[] newNotes = new Note[notes.length];
        for (int i = 0; i < notes.length; i++) {
            newNotes[i] = notes[i].shift(shift);
        }

        return new Tuning(newNotes);
    }
}
