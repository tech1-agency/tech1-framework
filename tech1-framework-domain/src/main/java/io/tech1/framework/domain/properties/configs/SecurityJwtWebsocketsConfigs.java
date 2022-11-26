package io.tech1.framework.domain.properties.configs;

import io.tech1.framework.domain.properties.annotations.MandatoryProperty;
import io.tech1.framework.domain.properties.annotations.NonMandatoryProperty;
import io.tech1.framework.domain.properties.configs.security.jwt.websockets.CsrfConfigs;
import io.tech1.framework.domain.properties.configs.security.jwt.websockets.MessageBrokerRegistryConfigs;
import io.tech1.framework.domain.properties.configs.security.jwt.websockets.StompEndpointRegistryConfigs;
import io.tech1.framework.domain.properties.configs.security.jwt.websockets.WebsocketsFeaturesConfigs;
import lombok.Data;
import lombok.EqualsAndHashCode;

// Lombok (property-based)
@Data
@EqualsAndHashCode(callSuper = true)
public class SecurityJwtWebsocketsConfigs extends AbstractPropertiesConfigs {
    @MandatoryProperty
    private CsrfConfigs csrfConfigs;
    @MandatoryProperty
    private StompEndpointRegistryConfigs stompConfigs;
    @MandatoryProperty
    private MessageBrokerRegistryConfigs brokerConfigs;
    @NonMandatoryProperty
    private WebsocketsFeaturesConfigs featuresConfigs;

    // NOTE: test-purposes
    public static SecurityJwtWebsocketsConfigs of(
            CsrfConfigs csrfConfigs,
            StompEndpointRegistryConfigs stompConfigs,
            MessageBrokerRegistryConfigs brokerConfigs,
            WebsocketsFeaturesConfigs featuresConfigs
    ) {
        var instance = new SecurityJwtWebsocketsConfigs();
        instance.csrfConfigs = csrfConfigs;
        instance.stompConfigs = stompConfigs;
        instance.brokerConfigs = brokerConfigs;
        instance.featuresConfigs = featuresConfigs;
        return instance;
    }
}
