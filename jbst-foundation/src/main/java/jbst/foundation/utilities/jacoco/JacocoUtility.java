package jbst.foundation.utilities.jacoco;

import lombok.experimental.UtilityClass;

import java.lang.reflect.Constructor;

@UtilityClass
public class JacocoUtility {

    public static void classCoverageHook(Class<?> clazz) {
        try {
            // default constructor
            clazz.getDeclaredConstructor().newInstance();
        } catch (ReflectiveOperationException e1) {
            try {
                // private constructor
                Constructor<?> constructor = clazz.getDeclaredConstructor();
                constructor.setAccessible(true);
                constructor.newInstance();
            } catch (ReflectiveOperationException e2) {
                // ignore
            }
        }
    }
}
