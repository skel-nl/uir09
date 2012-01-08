package skel.misc.util;

/**
 * @author Alexey Dudarev
 */
public class EnumResolver<T extends Enum<T>> {

    private final T[] enumConstants;

    private EnumResolver(Class<T> enumClass) {
        Validate.notNull(enumClass);

        enumConstants = enumClass.getEnumConstants();
    }

    public static <E extends Enum<E>> EnumResolver<E> er(Class<E> enumClass) {
        return new EnumResolver<E>(enumClass);
    }

    public T resolve(int i) {
        return enumConstants[i];
    }
}
