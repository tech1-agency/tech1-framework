package io.tech1.framework.domain.properties.configs;

import io.tech1.framework.domain.properties.annotations.MandatoryProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

// Lombok (property-based)
@Data
@EqualsAndHashCode(callSuper = true)
public class EventsConfigs extends AbstractPropertiesConfigs {
    @MandatoryProperty
    private String threadNamePrefix;

    // NOTE: test-purposes
    public static EventsConfigs of(
            String threadNamePrefix
    ) {
        var instance = new EventsConfigs();
        instance.threadNamePrefix = threadNamePrefix;
        return instance;
    }
}
