package org.server1;

import java.lang.reflect.Field;

public class Reflect {
    public static double getDoubleFromObjectField(Field field, Object object) {
        double result;
        boolean canAccess = field.canAccess(object);
        field.setAccessible(true);

        try {
            result = field.getDouble(object);
        } catch (IllegalAccessException e) {
            result = Double.NaN;
        } finally {
            field.setAccessible(canAccess);
        }

        return result;
    }
}
