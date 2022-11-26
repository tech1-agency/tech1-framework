package io.tech1.framework.domain.properties.configs.security.jwt.websockets;

import io.tech1.framework.domain.properties.annotations.NonMandatoryProperty;
import io.tech1.framework.domain.properties.configs.AbstractPropertiesConfigs;
import lombok.Data;
import lombok.EqualsAndHashCode;

// Lombok (property-based)
@Data
@EqualsAndHashCode(callSuper = true)
public class WebsocketsFeaturesConfigs extends AbstractPropertiesConfigs {
    @NonMandatoryProperty
    private WebsocketsFeatureHardwareConfigs hardwareConfigs;

    // NOTE: test-purposes
    public static WebsocketsFeaturesConfigs of(
            WebsocketsFeatureHardwareConfigs hardwareConfigs
    ) {
        var instance = new WebsocketsFeaturesConfigs();
        instance.hardwareConfigs = hardwareConfigs;
        return instance;
    }
}
