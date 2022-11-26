package io.tech1.framework.domain.properties.configs.security.jwt.websockets;

import io.tech1.framework.domain.properties.annotations.MandatoryProperty;
import io.tech1.framework.domain.properties.configs.AbstractPropertiesConfigs;
import lombok.Data;
import lombok.EqualsAndHashCode;

// Lombok (property-based)
@Data
@EqualsAndHashCode(callSuper = true)
public class MessageBrokerRegistryConfigs extends AbstractPropertiesConfigs {
    // INFO: spring support list of prefixes as varargs
    @MandatoryProperty
    private String applicationDestinationPrefix;
    // INFO: spring support list of destinations as varargs
    @MandatoryProperty
    private String simpleDestination;
    @MandatoryProperty
    private String userDestinationPrefix;

    // NOTE: test-purposes
    public static MessageBrokerRegistryConfigs of(
            String applicationDestinationPrefix,
            String simpleDestination,
            String userDestinationPrefix
    ) {
        var instance = new MessageBrokerRegistryConfigs();
        instance.applicationDestinationPrefix = applicationDestinationPrefix;
        instance.simpleDestination = simpleDestination;
        instance.userDestinationPrefix = userDestinationPrefix;
        return instance;
    }
}
