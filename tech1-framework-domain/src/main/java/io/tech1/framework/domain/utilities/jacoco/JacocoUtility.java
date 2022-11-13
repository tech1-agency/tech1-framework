package io.tech1.framework.domain.utilities.jacoco;

import lombok.experimental.UtilityClass;
import lombok.extern.slf4j.Slf4j;

import java.lang.reflect.Constructor;

@Slf4j
@UtilityClass
public class JacocoUtility {

    public static void classCoverageHook(Class<?> clazz) {
        try {
            // default constructor
            clazz.getDeclaredConstructor().newInstance();
        } catch (ReflectiveOperationException e1) {
            LOGGER.error("Cannot initialize clazz by default constructor: `{}`", clazz.getCanonicalName(), e1);
            try {
                // private constructor
                Constructor<?> constructor = clazz.getDeclaredConstructor();
                constructor.setAccessible(true);
                constructor.newInstance();
            } catch (ReflectiveOperationException e2) {
                LOGGER.error("Cannot initialize clazz by private constructor: `{}`", clazz.getCanonicalName(), e2);
            }
        }
    }
}
