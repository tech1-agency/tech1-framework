package io.tech1.framework.domain.properties.configs.security.jwt.websockets;

import io.tech1.framework.domain.properties.annotations.MandatoryProperty;
import io.tech1.framework.domain.properties.annotations.NonMandatoryProperty;
import io.tech1.framework.domain.properties.configs.AbstractPropertiesToggleConfigs;
import lombok.Data;
import lombok.EqualsAndHashCode;

// Lombok (property-based)
@Data
@EqualsAndHashCode(callSuper = true)
public class WebsocketsFeatureHardwareConfigs extends AbstractPropertiesToggleConfigs {
    @MandatoryProperty
    private boolean enabled;
    @NonMandatoryProperty
    private String userDestination;

    // NOTE: test-purposes
    public static WebsocketsFeatureHardwareConfigs of(
            boolean enabled,
            String userDestination
    ) {
        var instance = new WebsocketsFeatureHardwareConfigs();
        instance.enabled = enabled;
        instance.userDestination = userDestination;
        return instance;
    }
}
