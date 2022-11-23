package io.tech1.framework.domain.tests.classes;

import io.tech1.framework.domain.properties.annotations.MandatoryProperty;
import io.tech1.framework.domain.properties.base.ScheduledJob;
import io.tech1.framework.domain.properties.base.SpringLogging;
import io.tech1.framework.domain.properties.base.SpringServer;
import io.tech1.framework.domain.properties.configs.AbstractPropertiesConfigs;
import lombok.Data;
import lombok.EqualsAndHashCode;

// Lombok (property-based)
@Data
@EqualsAndHashCode(callSuper = true)
public class NotUsedPropertiesConfigs extends AbstractPropertiesConfigs {
    @MandatoryProperty
    private ScheduledJob scheduledJob;
    @MandatoryProperty
    private SpringServer springServer;
    @MandatoryProperty
    private SpringLogging springLogging;

    // NOTE: test-NotUsedPropertiesConfigs
    public static NotUsedPropertiesConfigs of(
            ScheduledJob scheduledJob,
            SpringServer springServer,
            SpringLogging springLogging
    ) {
        var instance = new NotUsedPropertiesConfigs();
        instance.scheduledJob = scheduledJob;
        instance.springServer = springServer;
        instance.springLogging = springLogging;
        return instance;
    }
}
