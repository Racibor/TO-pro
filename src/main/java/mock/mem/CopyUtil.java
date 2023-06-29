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

    private static Object get(Field field, Object target) throws IllegalAccessException {
        setAccesible(field, true);

        Object var3;
        try {
            var3 = field.get(target);
        } catch (IllegalArgumentException | IllegalAccessException var8) {
            throw var8;
        } catch (RuntimeException var9) {
            throw new IllegalStateException("Could not read " + field + " from " + target, var9);
        } finally {
            setAccesible(field, false);
        }

        return var3;
//        //setAccesible(field, true);
//
//        Object var3;
//        try {
//            MethodHandles.Lookup lookup = MethodHandles.lookup();
//            MethodHandle getter = MethodHandles
//                    .privateLookupIn(field.getDeclaringClass(), lookup)
//                    .unreflectGetter(field);
//            //getter.bindTo(field.getDeclaringClass());
//            var3 = getter.invokeExact();
//            return var3;
//            //var3 = field.get(target);
//        } catch (IllegalArgumentException | IllegalAccessException var8) {
//            var8.printStackTrace();;
//            throw var8;
//        } catch (RuntimeException var9) {
//            var9.printStackTrace();
//            throw new IllegalStateException("Could not read " + field + " from " + target, var9);
//        } catch (Throwable e) {
//            e.printStackTrace();
//        } finally {
//            //setAccesible(field, false);
//        }
//
//        return null;
    }

    private static void set(Field field, Object target, Object value) throws IllegalAccessException {
        setAccesible(field, true);

        try {
            field.set(target, value);
        } catch (IllegalArgumentException | IllegalAccessException var9) {
            throw var9;
        } catch (RuntimeException var10) {
            throw new IllegalStateException("Could not write " + field + " to " + target, var10);
        } finally {
            setAccesible(field, false);
        }

    }

    private static void setAccesible(AccessibleObject object, boolean value) {
        try {
            object.setAccessible(value);
        } catch (RuntimeException var3) {
            var3.printStackTrace();
        }

    }


}
