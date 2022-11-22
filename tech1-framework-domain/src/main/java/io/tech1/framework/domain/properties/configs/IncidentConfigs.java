package io.tech1.framework.domain.properties.configs;

import io.tech1.framework.domain.properties.annotations.MandatoryProperty;
import io.tech1.framework.domain.properties.annotations.NonMandatoryProperty;
import io.tech1.framework.domain.properties.base.RemoteServer;
import io.tech1.framework.domain.properties.configs.incidents.AbstractIncidentFeatureConfigs;
import io.tech1.framework.domain.properties.configs.incidents.IncidentFeaturesConfigs;
import lombok.Data;
import lombok.EqualsAndHashCode;

import static java.lang.Boolean.TRUE;
import static java.util.Objects.isNull;

// Lombok (property-based)
@Data
@EqualsAndHashCode(callSuper = true)
public class IncidentConfigs extends AbstractPropertiesToggleConfigs implements AbstractIncidentFeatureConfigs {
    @MandatoryProperty
    private boolean enabled;
    @NonMandatoryProperty
    private RemoteServer remoteServer;
    @NonMandatoryProperty
    private IncidentFeaturesConfigs features;

    // NOTE: test-purposes
    public static IncidentConfigs of(
            boolean enabled,
            RemoteServer remoteServer,
            IncidentFeaturesConfigs features
    ) {
        var instance = new IncidentConfigs();
        instance.enabled = enabled;
        instance.remoteServer = remoteServer;
        instance.features = features;
        return instance;
    }

    @Override
    public void assertProperties() {
        super.assertProperties();

        var loginFailureUsernamePassword = this.features.getLoginFailureUsernamePassword();
        var loginFailureUsernameMaskedPassword = this.features.getLoginFailureUsernameMaskedPassword();

        if (TRUE.equals(loginFailureUsernamePassword.isEnabled()) && TRUE.equals(loginFailureUsernameMaskedPassword.isEnabled())) {
            throw new IllegalArgumentException("Please configure login failure incident feature. Only one feature type could be enabled");
        }
    }

    @Override
    public void configureRequiredIncidentsIfMissing() {
        if (isNull(this.features)) {
            this.features = new IncidentFeaturesConfigs();
        }
        this.features.configureRequiredIncidentsIfMissing();
    }
}
