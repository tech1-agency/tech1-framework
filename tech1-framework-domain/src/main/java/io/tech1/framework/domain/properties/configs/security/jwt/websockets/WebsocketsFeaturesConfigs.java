package io.tech1.framework.domain.properties.configs.security.jwt.websockets;

import io.tech1.framework.domain.properties.annotations.NonMandatoryProperty;
import io.tech1.framework.domain.properties.configs.AbstractPropertiesConfigs;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.boot.context.properties.ConstructorBinding;

// Lombok (property-based)
@AllArgsConstructor(onConstructor = @__({@ConstructorBinding}))
@Data
@EqualsAndHashCode(callSuper = true)
public class WebsocketsFeaturesConfigs extends AbstractPropertiesConfigs {
    @NonMandatoryProperty
    private WebsocketsFeatureHardwareConfigs hardwareConfigs;

    public static WebsocketsFeaturesConfigs testsHardcoded() {
        return new WebsocketsFeaturesConfigs(
                WebsocketsFeatureHardwareConfigs.testsHardcoded()
        );
    }
}
