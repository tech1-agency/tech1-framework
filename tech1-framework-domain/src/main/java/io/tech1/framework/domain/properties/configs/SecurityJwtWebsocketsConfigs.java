package io.tech1.framework.domain.properties.configs;

import io.tech1.framework.domain.properties.annotations.MandatoryProperty;
import io.tech1.framework.domain.properties.annotations.NonMandatoryProperty;
import io.tech1.framework.domain.properties.configs.security.jwt.websockets.CsrfConfigs;
import io.tech1.framework.domain.properties.configs.security.jwt.websockets.MessageBrokerRegistryConfigs;
import io.tech1.framework.domain.properties.configs.security.jwt.websockets.StompEndpointRegistryConfigs;
import io.tech1.framework.domain.properties.configs.security.jwt.websockets.WebsocketsFeaturesConfigs;
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
    @NonMandatoryProperty
    private WebsocketsFeaturesConfigs featuresConfigs;

    public static SecurityJwtWebsocketsConfigs testsHardcoded() {
        return new SecurityJwtWebsocketsConfigs(
                CsrfConfigs.testsHardcoded(),
                StompEndpointRegistryConfigs.testsHardcoded(),
                MessageBrokerRegistryConfigs.testsHardcoded(),
                WebsocketsFeaturesConfigs.testsHardcoded()
        );
    }

    public static SecurityJwtWebsocketsConfigs random() {
        return new SecurityJwtWebsocketsConfigs(
                CsrfConfigs.random(),
                StompEndpointRegistryConfigs.random(),
                MessageBrokerRegistryConfigs.random(),
                WebsocketsFeaturesConfigs.random()
        );
    }

    @Override
    public boolean isParentPropertiesNode() {
        return true;
    }
}
