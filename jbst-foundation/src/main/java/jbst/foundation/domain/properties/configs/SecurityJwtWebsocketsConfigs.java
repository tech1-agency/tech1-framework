package jbst.foundation.domain.properties.configs;

import jbst.foundation.domain.properties.annotations.MandatoryProperty;
import jbst.foundation.domain.properties.annotations.NonMandatoryProperty;
import jbst.foundation.domain.properties.configs.security.jwt.websockets.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.boot.context.properties.bind.ConstructorBinding;

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

    public static SecurityJwtWebsocketsConfigs hardcoded() {
        return new SecurityJwtWebsocketsConfigs(
                CsrfConfigs.hardcoded(),
                StompEndpointRegistryConfigs.hardcoded(),
                MessageBrokerRegistryConfigs.hardcoded(),
                WebsocketsTemplateConfigs.hardcoded(),
                WebsocketsFeaturesConfigs.hardcoded()
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
