package skel.misc.note;

import java.util.HashMap;
import java.util.Map;

/**
 * @author Alexey Dudarev
 */
public class Tuning {

    private static final Tuning E = new Tuning(
            new Note(OctaveNote.MI, 4),
            new Note(OctaveNote.SI, 3),
            new Note(OctaveNote.SOL, 3),
            new Note(OctaveNote.RE, 3),
            new Note(OctaveNote.LA, 2),
            new Note(OctaveNote.MI, 2)
    );

    private static final Tuning DROP_D = new Tuning(
            new Note(OctaveNote.MI, 4),
            new Note(OctaveNote.SI, 3),
            new Note(OctaveNote.SOL, 3),
            new Note(OctaveNote.RE, 3),
            new Note(OctaveNote.LA, 2),
            new Note(OctaveNote.RE, 2)
    );

    private static final Tuning OPEN_D = new Tuning(
            new Note(OctaveNote.RE, 4),
            new Note(OctaveNote.LA, 3),
            new Note(OctaveNote.FA_SHARP, 3),
            new Note(OctaveNote.RE, 3),
            new Note(OctaveNote.LA, 2),
            new Note(OctaveNote.RE, 2)
    );

    private static final Tuning OPEN_G = new Tuning(
            new Note(OctaveNote.RE, 4),
            new Note(OctaveNote.SI, 3),
            new Note(OctaveNote.SOL, 3),
            new Note(OctaveNote.RE, 3),
            new Note(OctaveNote.SOL, 2),
            new Note(OctaveNote.RE, 2)
    );

    private static final Map<String, Tuning> tuningsMap = new HashMap<String, Tuning>() {{
        put("E", E);
        put("DROP_D", DROP_D);
        put("OPEN_D", OPEN_D);
        put("OPEN_G", OPEN_G);
    }};

    private final Note[] notes;

    private Tuning(Note... strings) {
        this.notes = strings;
    }

    public Note getNote(int stringN) {
        return notes[stringN];
    }

    public static Tuning tuning(String tuningName) {
        return tuningsMap.get(tuningName);
    }

    public Tuning shift(int shift) {
        Note[] newNotes = new Note[notes.length];
        for (int i = 0; i < notes.length; i++) {
            newNotes[i] = notes[i].shift(shift);
        }

        return new Tuning(newNotes);
    }
}
