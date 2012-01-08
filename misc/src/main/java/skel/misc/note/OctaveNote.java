package skel.misc.note;

import skel.misc.util.EnumResolver;

/**
 * @author Alexey Dudarev
 */
public enum OctaveNote {

    DO("C"),
    DO_SHARP("C♯"),
    RE("D"),
    RE_SHARP("D♯"),
    MI("E"),
    FA("F"),
    FA_SHARP("F♯"),
    SOL("G"),
    SOL_SHARP("G♯"),
    LA("A"),
    LA_SHARP("A♯"),
    SI("B");

    public static final EnumResolver<OctaveNote> R = EnumResolver.er(OctaveNote.class);

    private final String label;

    private OctaveNote(String label) {
        this.label = label;
    }

    public String getLabel() {
        return label;
    }
}
