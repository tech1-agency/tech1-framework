package io.tech1.framework.domain.properties.base;

import io.tech1.framework.domain.properties.annotations.MandatoryProperty;
import lombok.Data;

import java.util.concurrent.TimeUnit;

// Lombok (property-based)
@Data
public class SchedulerConfiguration {
    @MandatoryProperty
    private long initialDelay;
    @MandatoryProperty
    private long delay;
    @MandatoryProperty
    private TimeUnit unit;

    // NOTE: test-purposes
    public static SchedulerConfiguration of(
            long initialDelay,
            long delay,
            TimeUnit unit
    ) {
        var instance = new SchedulerConfiguration();
        instance.initialDelay = initialDelay;
        instance.delay = delay;
        instance.unit = unit;
        return instance;
    }

    public io.tech1.framework.domain.time.SchedulerConfiguration getSchedulerConfiguration() {
        return new io.tech1.framework.domain.time.SchedulerConfiguration(
                this.initialDelay,
                this.delay,
                this.unit
        );
    }

    @Override
    public String toString() {
        return "[" + this.initialDelay + ", " + this.delay + ", " + this.unit + "]";
    }
}
