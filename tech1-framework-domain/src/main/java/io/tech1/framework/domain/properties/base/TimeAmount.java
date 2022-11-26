package io.tech1.framework.domain.properties.base;

import lombok.Data;

import java.time.temporal.ChronoUnit;

// Lombok (property-based)
@Data
public class TimeAmount {
    private long amount;
    private ChronoUnit unit;

    // NOTE: test-purposes
    public static TimeAmount of(
            long amount,
            ChronoUnit unit
    ) {
        var instance = new TimeAmount();
        instance.amount = amount;
        instance.unit = unit;
        return instance;
    }

    public io.tech1.framework.domain.time.TimeAmount getTimeAmount() {
        return io.tech1.framework.domain.time.TimeAmount.of(
                this.amount,
                this.unit
        );
    }

    @Override
    public String toString() {
        return this.amount + " " + this.unit;
    }
}
