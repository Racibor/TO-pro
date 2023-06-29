package mock;

import java.util.HashMap;
import java.util.Map;

public final class Primitives {
    private static final Map<Class<?>, Class<?>> PRIMITIVE_TYPES = new HashMap();
    private static final Map<Class<?>, Object> PRIMITIVE_OR_WRAPPER_DEFAULT_VALUES = new HashMap();

    public static <T> Class<T> primitiveTypeOf(Class<T> clazz) {
        return clazz.isPrimitive() ? clazz : (Class)PRIMITIVE_TYPES.get(clazz);
    }

    public static boolean isPrimitiveOrWrapper(Class<?> type) {
        return PRIMITIVE_OR_WRAPPER_DEFAULT_VALUES.containsKey(type);
    }

    public static boolean isAssignableFromWrapper(Class<?> valueClass, Class<?> referenceType) {
        return isPrimitiveOrWrapper(valueClass) && isPrimitiveOrWrapper(referenceType) ? primitiveTypeOf(valueClass).isAssignableFrom(primitiveTypeOf(referenceType)) : false;
    }

    public static <T> T defaultValue(Class<T> primitiveOrWrapperType) {
        return (T) PRIMITIVE_OR_WRAPPER_DEFAULT_VALUES.get(primitiveOrWrapperType);
    }

    private Primitives() {
    }

    static {
        PRIMITIVE_TYPES.put(Boolean.class, Boolean.TYPE);
        PRIMITIVE_TYPES.put(Character.class, Character.TYPE);
        PRIMITIVE_TYPES.put(Byte.class, Byte.TYPE);
        PRIMITIVE_TYPES.put(Short.class, Short.TYPE);
        PRIMITIVE_TYPES.put(Integer.class, Integer.TYPE);
        PRIMITIVE_TYPES.put(Long.class, Long.TYPE);
        PRIMITIVE_TYPES.put(Float.class, Float.TYPE);
        PRIMITIVE_TYPES.put(Double.class, Double.TYPE);
        PRIMITIVE_OR_WRAPPER_DEFAULT_VALUES.put(Boolean.class, false);
        PRIMITIVE_OR_WRAPPER_DEFAULT_VALUES.put(Character.class, '\u0000');
        PRIMITIVE_OR_WRAPPER_DEFAULT_VALUES.put(Byte.class, (byte)0);
        PRIMITIVE_OR_WRAPPER_DEFAULT_VALUES.put(Short.class, Short.valueOf((short)0));
        PRIMITIVE_OR_WRAPPER_DEFAULT_VALUES.put(Integer.class, 0);
        PRIMITIVE_OR_WRAPPER_DEFAULT_VALUES.put(Long.class, 0L);
        PRIMITIVE_OR_WRAPPER_DEFAULT_VALUES.put(Float.class, 0.0F);
        PRIMITIVE_OR_WRAPPER_DEFAULT_VALUES.put(Double.class, 0.0D);
        PRIMITIVE_OR_WRAPPER_DEFAULT_VALUES.put(Boolean.TYPE, false);
        PRIMITIVE_OR_WRAPPER_DEFAULT_VALUES.put(Character.TYPE, '\u0000');
        PRIMITIVE_OR_WRAPPER_DEFAULT_VALUES.put(Byte.TYPE, (byte)0);
        PRIMITIVE_OR_WRAPPER_DEFAULT_VALUES.put(Short.TYPE, Short.valueOf((short)0));
        PRIMITIVE_OR_WRAPPER_DEFAULT_VALUES.put(Integer.TYPE, 0);
        PRIMITIVE_OR_WRAPPER_DEFAULT_VALUES.put(Long.TYPE, 0L);
        PRIMITIVE_OR_WRAPPER_DEFAULT_VALUES.put(Float.TYPE, 0.0F);
        PRIMITIVE_OR_WRAPPER_DEFAULT_VALUES.put(Double.TYPE, 0.0D);
    }
}
