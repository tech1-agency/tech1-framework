package io.tech1.framework.domain.properties.configs.security.jwt;

import io.tech1.framework.domain.properties.annotations.MandatoryProperty;
import io.tech1.framework.domain.properties.base.SecurityJwtIncidentType;
import io.tech1.framework.domain.properties.configs.AbstractPropertiesConfigs;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.Map;

import static java.lang.Boolean.TRUE;

// Lombok (property-based)
@Data
@EqualsAndHashCode(callSuper = true)
public class IncidentsConfigs extends AbstractPropertiesConfigs {
    @MandatoryProperty
    private Map<SecurityJwtIncidentType, Boolean> typesConfigs;

    // NOTE: test-purposes
    public static IncidentsConfigs of(
            Map<SecurityJwtIncidentType, Boolean> typesConfigs
    ) {
        var instance = new IncidentsConfigs();
        instance.typesConfigs = typesConfigs;
        return instance;
    }

    public boolean isEnabled(SecurityJwtIncidentType type) {
        return TRUE.equals(this.typesConfigs.get(type));
    }
}
