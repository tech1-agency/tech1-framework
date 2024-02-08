package io.tech1.framework.domain.tests.classes;

import io.tech1.framework.domain.properties.annotations.MandatoryProperty;
import io.tech1.framework.domain.properties.base.ScheduledJob;
import io.tech1.framework.domain.properties.base.SpringLogging;
import io.tech1.framework.domain.properties.base.SpringServer;
import io.tech1.framework.domain.properties.configs.AbstractPropertiesConfigsV2;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.boot.context.properties.ConstructorBinding;

// Lombok (property-based)
@AllArgsConstructor(onConstructor = @__({@ConstructorBinding}))
@Data
@EqualsAndHashCode(callSuper = true)
public class NotUsedPropertiesConfigs extends AbstractPropertiesConfigsV2 {
    @MandatoryProperty
    private final ScheduledJob scheduledJob;
    @MandatoryProperty
    private final SpringServer springServer;
    @MandatoryProperty
    private final SpringLogging springLogging;
}
