package io.tech1.framework.domain.properties.configs.security.jwt.websockets;

import io.tech1.framework.domain.properties.annotations.MandatoryProperty;
import io.tech1.framework.domain.properties.configs.AbstractPropertiesConfigs;
import lombok.Data;
import lombok.EqualsAndHashCode;

// Lombok (property-based)
@Data
@EqualsAndHashCode(callSuper = true)
public class StompEndpointRegistryConfigs extends AbstractPropertiesConfigs {
    // WARNING: spring support list of endpoints as varargs
    @MandatoryProperty
    private String endpoint;

    // NOTE: test-purposes
    public static StompEndpointRegistryConfigs of(
            String endpoint
    ) {
        var instance = new StompEndpointRegistryConfigs();
        instance.endpoint = endpoint;
        return instance;
    }
}
