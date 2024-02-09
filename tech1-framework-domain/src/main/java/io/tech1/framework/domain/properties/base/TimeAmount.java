package io.tech1.framework.domain.properties.base;

import io.tech1.framework.domain.properties.annotations.MandatoryProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.boot.context.properties.ConstructorBinding;

import java.time.temporal.ChronoUnit;

import static io.tech1.framework.domain.utilities.random.RandomUtility.randomChronoUnit;
import static io.tech1.framework.domain.utilities.random.RandomUtility.randomIntegerGreaterThanZeroByBounds;
import static java.time.temporal.ChronoUnit.HOURS;

// Lombok (property-based)
@AllArgsConstructor(onConstructor = @__({@ConstructorBinding}))
@Data
@EqualsAndHashCode(callSuper = true)
public class TimeAmount extends AbstractPropertyConfigs {
    @MandatoryProperty
    private final long amount;
    @MandatoryProperty
    private final ChronoUnit unit;

    public static TimeAmount testsHardcoded() {
        return new TimeAmount(12L, HOURS);
    }

    public static TimeAmount random() {
        return new TimeAmount(randomIntegerGreaterThanZeroByBounds(1, 10), randomChronoUnit());
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
