package io.tech1.framework.domain.utilities.concurrent;

import lombok.experimental.UtilityClass;

import java.util.concurrent.TimeUnit;

@UtilityClass
public class SleepUtility {

    public static void sleepMilliseconds(long timeout) {
        sleep(TimeUnit.MILLISECONDS, timeout);
    }

    public static void sleep(TimeUnit timeUnit, long timeout) {
        try {
            timeUnit.sleep(timeout);
        } catch (InterruptedException ex) {
            // ignore
        }
    }
}
