package jbst.foundation.domain.properties.configs.security.jwt.websockets;

import jbst.foundation.domain.properties.annotations.MandatoryProperty;
import jbst.foundation.domain.properties.base.AbstractPropertyConfigs;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.boot.context.properties.bind.ConstructorBinding;

import static jbst.foundation.utilities.random.RandomUtility.randomString;

// Lombok (property-based)
@AllArgsConstructor(onConstructor = @__({@ConstructorBinding}))
@Data
@EqualsAndHashCode(callSuper = true)
public class StompEndpointRegistryConfigs extends AbstractPropertyConfigs {
    // Spring support list of endpoints as varargs
    @MandatoryProperty
    private final String endpoint;

    public static StompEndpointRegistryConfigs hardcoded() {
        return new StompEndpointRegistryConfigs("/endpoint");
    }

    public static StompEndpointRegistryConfigs random() {
        return new StompEndpointRegistryConfigs(randomString());
    }
}
