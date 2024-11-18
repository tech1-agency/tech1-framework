package tech1.framework.foundation.domain.properties.base;

import tech1.framework.foundation.domain.properties.annotations.MandatoryProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.boot.context.properties.bind.ConstructorBinding;

import java.util.concurrent.TimeUnit;

import static tech1.framework.foundation.utilities.random.RandomUtility.randomLongGreaterThanZeroByBounds;
import static tech1.framework.foundation.utilities.random.RandomUtility.randomTimeUnit;
import static java.util.concurrent.TimeUnit.SECONDS;

// Lombok (property-based)
@AllArgsConstructor(onConstructor = @__({@ConstructorBinding}))
@Data
@EqualsAndHashCode(callSuper = true)
public class SchedulerConfiguration extends AbstractPropertyConfigs {
    @MandatoryProperty
    private final Long initialDelay;
    @MandatoryProperty
    private final Long delay;
    @MandatoryProperty
    private final TimeUnit unit;

    public static SchedulerConfiguration testsHardcoded() {
        return new SchedulerConfiguration(30L, 30L, SECONDS);
    }

    public static SchedulerConfiguration random() {
        return new SchedulerConfiguration(
                randomLongGreaterThanZeroByBounds(15, 45),
                randomLongGreaterThanZeroByBounds(15, 45),
                randomTimeUnit()
        );
    }

    public tech1.framework.foundation.domain.time.SchedulerConfiguration getSchedulerConfiguration() {
        return new tech1.framework.foundation.domain.time.SchedulerConfiguration(
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
