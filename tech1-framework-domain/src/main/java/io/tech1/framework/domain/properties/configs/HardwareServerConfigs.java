package io.tech1.framework.domain.properties.configs;

import io.tech1.framework.domain.properties.annotations.MandatoryProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

// Lombok (property-based)
@Data
@EqualsAndHashCode(callSuper = true)
public class HardwareServerConfigs extends AbstractPropertiesConfigs {
    @MandatoryProperty
    private String baseURL;

    // NOTE: test-purposes
    public static HardwareServerConfigs of(
            String baseURL
    ) {
        var instance = new HardwareServerConfigs();
        instance.baseURL = baseURL;
        return instance;
    }
}
