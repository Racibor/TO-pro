package mock.mem;

import java.lang.invoke.MethodHandle;
import java.lang.invoke.MethodHandles;
import java.lang.reflect.AccessibleObject;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;

public class CopyUtil {

    public static <T> void copy(T from, T to, Class<?> fromClazz) {
        while(fromClazz != Object.class) {
            copyValues(from, to, fromClazz);
            fromClazz = fromClazz.getSuperclass();
        }

    }

    private static <T> void copyValues(T from, T mock, Class<?> classFrom) {
        Field[] fields = classFrom.getDeclaredFields();
        Field[] var5 = fields;
        int var6 = fields.length;

        for(int var7 = 0; var7 < var6; ++var7) {
            Field field = var5[var7];
            if (!Modifier.isStatic(field.getModifiers())) {
                try {
                    Object value = get(field, from);
                    set(field, mock, value);
                } catch (Throwable var10) {
                    var10.printStackTrace();
                }
            }
        }

    }

    private static Object get(Field field, Object target) {
        setAccesible(field, true);

        Object ret;
        try {
            ret = field.get(target);
        } catch(Exception e) {
            throw new IllegalStateException("Could not read " + field + " from " + target);
        } finally {
            setAccesible(field, false);
        }

        return ret;
    }

    private static void set(Field field, Object target, Object value) throws IllegalAccessException {
        setAccesible(field, true);

        try {
            field.set(target, value);
        } finally {
            setAccesible(field, false);
        }

    }

    private static void setAccesible(AccessibleObject object, boolean value) {
        try {
            object.setAccessible(value);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }


}
