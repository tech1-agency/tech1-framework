package tech1.framework.foundation.domain.properties.configs;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.boot.context.properties.bind.ConstructorBinding;
import tech1.framework.foundation.domain.properties.annotations.MandatoryProperty;
import tech1.framework.foundation.domain.properties.annotations.NonMandatoryProperty;
import tech1.framework.foundation.domain.properties.configs.security.jwt.websockets.*;

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
