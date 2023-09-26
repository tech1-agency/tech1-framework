package io.tech1.framework.domain.properties.base;

import io.tech1.framework.domain.properties.annotations.MandatoryProperty;
import io.tech1.framework.domain.properties.configs.AbstractPropertiesConfigs;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.boot.context.properties.ConstructorBinding;

import java.util.concurrent.TimeUnit;

// Lombok (property-based)
@AllArgsConstructor(onConstructor = @__({@ConstructorBinding}))
@Data
@EqualsAndHashCode(callSuper = true)
public class SchedulerConfiguration extends AbstractPropertiesConfigs {
    @MandatoryProperty
    private final long initialDelay;
    @MandatoryProperty
    private final long delay;
    @MandatoryProperty
    private final TimeUnit unit;

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
