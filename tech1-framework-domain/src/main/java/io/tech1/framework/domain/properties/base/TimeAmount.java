package io.tech1.framework.domain.properties.base;

import io.tech1.framework.domain.properties.annotations.MandatoryProperty;
import io.tech1.framework.domain.properties.configs.AbstractPropertiesConfigs;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.boot.context.properties.ConstructorBinding;

import java.time.temporal.ChronoUnit;

// Lombok (property-based)
@AllArgsConstructor(onConstructor = @__({@ConstructorBinding}))
@Data
@EqualsAndHashCode(callSuper = true)
public class TimeAmount extends AbstractPropertiesConfigs {
    @MandatoryProperty
    private final long amount;
    @MandatoryProperty
    private final ChronoUnit unit;

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
