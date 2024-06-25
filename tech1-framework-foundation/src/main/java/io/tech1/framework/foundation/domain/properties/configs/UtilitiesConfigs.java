package io.tech1.framework.foundation.domain.properties.configs;

import io.tech1.framework.foundation.domain.properties.annotations.MandatoryProperty;
import io.tech1.framework.foundation.domain.properties.configs.utilities.GeoCountryFlagsConfigs;
import io.tech1.framework.foundation.domain.properties.configs.utilities.GeoLocationsConfigs;
import io.tech1.framework.foundation.domain.properties.configs.utilities.UserAgentConfigs;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.boot.context.properties.bind.ConstructorBinding;

// Lombok (property-based)
@AllArgsConstructor(onConstructor = @__({@ConstructorBinding}))
@Data
@EqualsAndHashCode(callSuper = true)
public class UtilitiesConfigs extends AbstractPropertiesConfigs {
    @MandatoryProperty
    private final GeoLocationsConfigs geoLocationsConfigs;
    @MandatoryProperty
    private final GeoCountryFlagsConfigs geoCountryFlagsConfigs;
    @MandatoryProperty
    private final UserAgentConfigs userAgentConfigs;

    public static UtilitiesConfigs testsHardcoded() {
        return new UtilitiesConfigs(
                GeoLocationsConfigs.disabled(),
                GeoCountryFlagsConfigs.enabled(),
                UserAgentConfigs.enabled()
        );
    }

    public static UtilitiesConfigs random() {
        return new UtilitiesConfigs(
                GeoLocationsConfigs.random(),
                GeoCountryFlagsConfigs.random(),
                UserAgentConfigs.random()
        );
    }

    @Override
    public boolean isParentPropertiesNode() {
        return true;
    }
}
