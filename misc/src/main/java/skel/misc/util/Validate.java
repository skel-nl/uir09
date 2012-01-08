package skel.misc.util;

/**
 * A little clone of org.apache.commons.lang.Validate
 *
 * @author Alexey Dudarev
 */
public final class Validate {

    private Validate() {
    }

    public static void isTrue(boolean expression, String message) {
        if (!expression) {
            throw new IllegalArgumentException(message);
        }
    }

    public static void notNull(Object object) {
        if (object == null) {
            throw new IllegalArgumentException("The validated object is null");
        }
    }
}
