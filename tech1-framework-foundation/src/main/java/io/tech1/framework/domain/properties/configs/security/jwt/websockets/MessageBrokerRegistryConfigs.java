package io.tech1.framework.domain.properties.configs.security.jwt.websockets;

import io.tech1.framework.domain.properties.annotations.MandatoryProperty;
import io.tech1.framework.domain.properties.base.AbstractPropertyConfigs;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.boot.context.properties.bind.ConstructorBinding;

import static io.tech1.framework.foundation.utilities.random.RandomUtility.randomString;

// Lombok (property-based)
@AllArgsConstructor(onConstructor = @__({@ConstructorBinding}))
@Data
@EqualsAndHashCode(callSuper = true)
public class MessageBrokerRegistryConfigs extends AbstractPropertyConfigs {
    // INFO: spring support list of prefixes as varargs
    @MandatoryProperty
    private final String applicationDestinationPrefix;
    // INFO: spring support list of destinations as varargs
    @MandatoryProperty
    private final String simpleDestination;
    @MandatoryProperty
    private final String userDestinationPrefix;

    public static MessageBrokerRegistryConfigs testsHardcoded() {
        return new MessageBrokerRegistryConfigs("/app", "/queue", "/user");
    }

    public static MessageBrokerRegistryConfigs random() {
        return new MessageBrokerRegistryConfigs(randomString(), randomString(), randomString());
    }
}
