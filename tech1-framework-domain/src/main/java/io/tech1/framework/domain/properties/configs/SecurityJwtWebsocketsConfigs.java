package io.tech1.framework.domain.properties.configs;

import io.tech1.framework.domain.properties.annotations.MandatoryProperty;
import io.tech1.framework.domain.properties.annotations.NonMandatoryProperty;
import io.tech1.framework.domain.properties.configs.security.jwt.websockets.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.boot.context.properties.ConstructorBinding;

// Lombok (property-based)
@AllArgsConstructor(onConstructor = @__({@ConstructorBinding}))
@Data
@EqualsAndHashCode(callSuper = true)
public class SecurityJwtWebsocketsConfigs extends AbstractPropertiesConfigs {
    @MandatoryProperty
    private final CsrfConfigs csrfConfigs;
    @MandatoryProperty
    private final StompEndpointRegistryConfigs stompConfigs;
    @MandatoryProperty
    private final MessageBrokerRegistryConfigs brokerConfigs;
    @MandatoryProperty
    private final WebsocketsTemplateConfigs templateConfigs;
    @NonMandatoryProperty
    private WebsocketsFeaturesConfigs featuresConfigs;

    public static SecurityJwtWebsocketsConfigs testsHardcoded() {
        return new SecurityJwtWebsocketsConfigs(
                CsrfConfigs.testsHardcoded(),
                StompEndpointRegistryConfigs.testsHardcoded(),
                MessageBrokerRegistryConfigs.testsHardcoded(),
                WebsocketsTemplateConfigs.testsHardcoded(),
                WebsocketsFeaturesConfigs.testsHardcoded()
        );
    }

    public static SecurityJwtWebsocketsConfigs random() {
        return new SecurityJwtWebsocketsConfigs(
                CsrfConfigs.random(),
                StompEndpointRegistryConfigs.random(),
                MessageBrokerRegistryConfigs.random(),
                WebsocketsTemplateConfigs.random(),
                WebsocketsFeaturesConfigs.random()
        );
    }

    @Override
    public boolean isParentPropertiesNode() {
        return true;
    }
}
