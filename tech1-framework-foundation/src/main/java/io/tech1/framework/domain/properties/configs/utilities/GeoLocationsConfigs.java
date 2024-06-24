package io.tech1.framework.domain.properties.configs.utilities;

import io.tech1.framework.domain.properties.annotations.MandatoryProperty;
import io.tech1.framework.domain.properties.base.AbstractPropertyConfigs;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.boot.context.properties.bind.ConstructorBinding;

import static io.tech1.framework.foundation.utilities.random.RandomUtility.randomBoolean;

// Lombok (property-based)
@AllArgsConstructor(onConstructor = @__({@ConstructorBinding}))
@Data
@EqualsAndHashCode(callSuper = true)
public class GeoLocationsConfigs extends AbstractPropertyConfigs {
    @MandatoryProperty
    private final Boolean geoLiteCityDatabaseEnabled;

    public static GeoLocationsConfigs testsHardcoded() {
        return new GeoLocationsConfigs(true);
    }

    public static GeoLocationsConfigs random() {
        return randomBoolean() ? enabled() : disabled();
    }

    public static GeoLocationsConfigs enabled() {
        return testsHardcoded();
    }

    public static GeoLocationsConfigs disabled() {
        return new GeoLocationsConfigs(false);
    }

    public boolean isGeoLiteCityDatabaseEnabled() {
        return this.geoLiteCityDatabaseEnabled;
    }
}
