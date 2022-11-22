package io.tech1.framework.domain.properties.configs.incidents;

import io.tech1.framework.domain.properties.annotations.NonMandatoryProperty;
import io.tech1.framework.domain.properties.configs.AbstractPropertiesToggleConfigs;
import lombok.Data;
import lombok.EqualsAndHashCode;

// Lombok (property-based)
@Data
@EqualsAndHashCode(callSuper = true)
public class IncidentFeatureConfigs extends AbstractPropertiesToggleConfigs implements AbstractIncidentFeatureConfigs {
    @NonMandatoryProperty
    private boolean enabled;

    // NOTE: test-purposes
    public static IncidentFeatureConfigs enabledIncidentFeatureConfigs() {
        var instance = new IncidentFeatureConfigs();
        instance.enabled = true;
        return instance;
    }

    // NOTE: test-purposes
    public static IncidentFeatureConfigs disabledIncidentFeatureConfigs() {
        var instance = new IncidentFeatureConfigs();
        instance.enabled = false;
        return instance;
    }

    @Override
    public void configureRequiredIncidentsIfMissing() {
        this.enabled = false;
    }
}
