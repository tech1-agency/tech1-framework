package io.tech1.framework.domain.properties.configs.security.jwt.websockets;

import io.tech1.framework.domain.properties.annotations.MandatoryProperty;
import io.tech1.framework.domain.properties.annotations.NonMandatoryProperty;
import io.tech1.framework.domain.properties.configs.AbstractPropertiesToggleConfigs;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.boot.context.properties.ConstructorBinding;

// Lombok (property-based)
@AllArgsConstructor(onConstructor = @__({@ConstructorBinding}))
@Data
@EqualsAndHashCode(callSuper = true)
public class WebsocketsFeatureHardwareConfigs extends AbstractPropertiesToggleConfigs {
    @MandatoryProperty
    private final boolean enabled;
    @NonMandatoryProperty
    private String userDestination;

    public static WebsocketsFeatureHardwareConfigs testsHardcoded() {
        return new WebsocketsFeatureHardwareConfigs(true, "/account");
    }
}
