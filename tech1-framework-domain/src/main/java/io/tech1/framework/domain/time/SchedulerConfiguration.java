package io.tech1.framework.domain.time;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

import java.util.concurrent.TimeUnit;

// Lombok
@Getter
@EqualsAndHashCode
@ToString
public class SchedulerConfiguration {
    private final long initialDelay;
    private final long delay;
    private final TimeUnit unit;
    private final long delayedSeconds;

    @JsonCreator
    public SchedulerConfiguration(
            @JsonProperty("initialDelay") long initialDelay,
            @JsonProperty("delay") long delay,
            @JsonProperty("unit") TimeUnit unit
    ) {
        this.initialDelay = initialDelay;
        this.delay = delay;
        this.unit = unit;
        this.delayedSeconds = this.unit.toSeconds(this.delay);
    }

    public static SchedulerConfiguration of(
            long initialDelay,
            long delay,
            TimeUnit unit
    ) {
        return new SchedulerConfiguration(
                initialDelay,
                delay,
                unit
        );
    }

    public static SchedulerConfiguration never() {
        return of(Long.MAX_VALUE, Long.MAX_VALUE, TimeUnit.DAYS);
    }
}
