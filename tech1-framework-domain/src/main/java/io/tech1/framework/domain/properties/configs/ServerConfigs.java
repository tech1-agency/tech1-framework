package io.tech1.framework.domain.properties.configs;

import io.tech1.framework.domain.properties.annotations.MandatoryProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

// Lombok (property-based)
@Data
@EqualsAndHashCode(callSuper = true)
public class ServerConfigs extends AbstractPropertiesConfigs {
    @MandatoryProperty
    private String name;
    @MandatoryProperty
    private String webclientURL;

    // NOTE: test-purposes
    public static ServerConfigs of(
            String name,
            String webclientURL
    ) {
        var instance = new ServerConfigs();
        instance.name = name;
        instance.webclientURL = webclientURL;
        return instance;
    }
}
