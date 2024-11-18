package jbst.foundation.domain.properties.configs.utilities;

import jbst.foundation.domain.properties.annotations.MandatoryProperty;
import jbst.foundation.domain.properties.base.AbstractTogglePropertyConfigs;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.boot.context.properties.bind.ConstructorBinding;

import static jbst.foundation.utilities.random.RandomUtility.randomBoolean;

@AllArgsConstructor(onConstructor = @__({@ConstructorBinding}))
@Data
@EqualsAndHashCode(callSuper = true)
public class GeoCountryFlagsConfigs extends AbstractTogglePropertyConfigs {
    @MandatoryProperty
    private final boolean enabled;

    public static GeoCountryFlagsConfigs hardcoded() {
        return new GeoCountryFlagsConfigs(true);
    }

    public static GeoCountryFlagsConfigs random() {
        return randomBoolean() ? enabled() : disabled();
    }

    public static GeoCountryFlagsConfigs enabled() {
        return hardcoded();
    }

    public static GeoCountryFlagsConfigs disabled() {
        return new GeoCountryFlagsConfigs(false);
    }
}
