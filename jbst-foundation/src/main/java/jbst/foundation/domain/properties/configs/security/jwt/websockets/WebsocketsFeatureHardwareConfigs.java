package jbst.foundation.domain.properties.configs.security.jwt.websockets;

import jbst.foundation.domain.properties.annotations.MandatoryProperty;
import jbst.foundation.domain.properties.annotations.MandatoryToggleProperty;
import jbst.foundation.domain.properties.base.AbstractTogglePropertyConfigs;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.boot.context.properties.bind.ConstructorBinding;

import static jbst.foundation.utilities.random.RandomUtility.randomBoolean;
import static jbst.foundation.utilities.random.RandomUtility.randomString;

// Lombok (property-based)
@AllArgsConstructor(onConstructor = @__({@ConstructorBinding}))
@Data
@EqualsAndHashCode(callSuper = true)
public class WebsocketsFeatureHardwareConfigs extends AbstractTogglePropertyConfigs {
    @MandatoryProperty
    private final boolean enabled;
    @MandatoryToggleProperty
    private String userDestination;

    public static WebsocketsFeatureHardwareConfigs testsHardcoded() {
        return new WebsocketsFeatureHardwareConfigs(true, "/accounts");
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
