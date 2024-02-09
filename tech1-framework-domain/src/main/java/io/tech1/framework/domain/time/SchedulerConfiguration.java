package io.tech1.framework.domain.time;

import java.util.concurrent.TimeUnit;

public record SchedulerConfiguration(
        long initialDelay,
        long delay,
        TimeUnit unit
) {

    public static SchedulerConfiguration never() {
        return new SchedulerConfiguration(
                Long.MAX_VALUE,
                Long.MAX_VALUE,
                TimeUnit.DAYS
        );
    }
}
