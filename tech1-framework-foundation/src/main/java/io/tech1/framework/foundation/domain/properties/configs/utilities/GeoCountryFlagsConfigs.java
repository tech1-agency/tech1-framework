package io.tech1.framework.foundation.domain.properties.configs.utilities;

import io.tech1.framework.foundation.domain.properties.annotations.MandatoryProperty;
import io.tech1.framework.foundation.domain.properties.base.AbstractTogglePropertyConfigs;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.boot.context.properties.bind.ConstructorBinding;

import static io.tech1.framework.foundation.utilities.random.RandomUtility.randomBoolean;

// Lombok (property-based)
@AllArgsConstructor(onConstructor = @__({@ConstructorBinding}))
@Data
@EqualsAndHashCode(callSuper = true)
public class GeoCountryFlagsConfigs extends AbstractTogglePropertyConfigs {
    @MandatoryProperty
    private final boolean enabled;

    public static GeoCountryFlagsConfigs testsHardcoded() {
        return new GeoCountryFlagsConfigs(true);
    }

    public static GeoCountryFlagsConfigs random() {
        return randomBoolean() ? enabled() : disabled();
    }

    public static GeoCountryFlagsConfigs enabled() {
        return testsHardcoded();
    }

    public static GeoCountryFlagsConfigs disabled() {
        return new GeoCountryFlagsConfigs(false);
    }
}
