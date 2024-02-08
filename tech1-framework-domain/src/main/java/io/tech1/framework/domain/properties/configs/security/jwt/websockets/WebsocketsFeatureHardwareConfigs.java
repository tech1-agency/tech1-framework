package io.tech1.framework.domain.properties.configs.security.jwt.websockets;

import io.tech1.framework.domain.properties.annotations.MandatoryProperty;
import io.tech1.framework.domain.properties.annotations.MandatoryToggleProperty;
import io.tech1.framework.domain.properties.configs.AbstractTogglePropertiesConfigs;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.boot.context.properties.ConstructorBinding;

import static io.tech1.framework.domain.utilities.random.RandomUtility.randomBoolean;
import static io.tech1.framework.domain.utilities.random.RandomUtility.randomString;

// Lombok (property-based)
@AllArgsConstructor(onConstructor = @__({@ConstructorBinding}))
@Data
@EqualsAndHashCode(callSuper = true)
public class WebsocketsFeatureHardwareConfigs extends AbstractTogglePropertiesConfigs {
    @MandatoryProperty
    private final boolean enabled;
    @MandatoryToggleProperty
    private String userDestination;

    public static WebsocketsFeatureHardwareConfigs testsHardcoded() {
        return new WebsocketsFeatureHardwareConfigs(true, "/account");
    }

    public static WebsocketsFeatureHardwareConfigs random() {
        return new WebsocketsFeatureHardwareConfigs(randomBoolean(), randomString());
    }

    public static WebsocketsFeatureHardwareConfigs enabled() {
        return testsHardcoded();
    }

    public static WebsocketsFeatureHardwareConfigs disabled() {
        return new WebsocketsFeatureHardwareConfigs(false, null);
    }
}
