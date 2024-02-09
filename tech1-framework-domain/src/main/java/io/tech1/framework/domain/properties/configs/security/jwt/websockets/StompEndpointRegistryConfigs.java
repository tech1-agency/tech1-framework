package io.tech1.framework.domain.properties.configs.security.jwt.websockets;

import io.tech1.framework.domain.properties.annotations.MandatoryProperty;
import io.tech1.framework.domain.properties.base.AbstractPropertyConfigs;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.boot.context.properties.ConstructorBinding;

import static io.tech1.framework.domain.utilities.random.RandomUtility.randomString;

// Lombok (property-based)
@AllArgsConstructor(onConstructor = @__({@ConstructorBinding}))
@Data
@EqualsAndHashCode(callSuper = true)
public class StompEndpointRegistryConfigs extends AbstractPropertyConfigs {
    // Spring support list of endpoints as varargs
    @MandatoryProperty
    private final String endpoint;

    public static StompEndpointRegistryConfigs testsHardcoded() {
        return new StompEndpointRegistryConfigs("/endpoint");
    }

    public static StompEndpointRegistryConfigs random() {
        return new StompEndpointRegistryConfigs(randomString());
    }
}
