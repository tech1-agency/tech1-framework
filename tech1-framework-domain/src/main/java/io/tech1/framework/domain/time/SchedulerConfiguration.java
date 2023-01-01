package io.tech1.framework.domain.time;

import lombok.Data;

import java.util.concurrent.TimeUnit;

// Lombok
@Data(staticConstructor = "of")
public class SchedulerConfiguration {
    private final long initialDelay;
    private final long delay;
    private final TimeUnit unit;

    public static SchedulerConfiguration never() {
        return of(
                Long.MAX_VALUE,
                Long.MAX_VALUE,
                TimeUnit.DAYS
        );
    }

    public long getDelayedSeconds() {
        return this.unit.toSeconds(this.delay);
    }
}
