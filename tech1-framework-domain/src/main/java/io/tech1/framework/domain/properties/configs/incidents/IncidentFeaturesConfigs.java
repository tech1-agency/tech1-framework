package io.tech1.framework.domain.properties.configs.incidents;

import io.tech1.framework.domain.properties.configs.AbstractPropertiesConfigs;
import lombok.Data;
import lombok.EqualsAndHashCode;

import static java.util.Objects.isNull;

// Lombok (property-based)
@Data
@EqualsAndHashCode(callSuper = true)
public class IncidentFeaturesConfigs extends AbstractPropertiesConfigs implements AbstractIncidentFeatureConfigs {
    private IncidentFeatureConfigs login;
    private IncidentFeatureConfigs loginFailureUsernamePassword;
    private IncidentFeatureConfigs loginFailureUsernameMaskedPassword;
    private IncidentFeatureConfigs logout;
    private IncidentFeatureConfigs sessionRefreshed;
    private IncidentFeatureConfigs sessionExpired;
    private IncidentFeatureConfigs register1;
    private IncidentFeatureConfigs register1Failure;

    // NOTE: test-purposes
    public static IncidentFeaturesConfigs of(
            IncidentFeatureConfigs login,
            IncidentFeatureConfigs loginFailureUsernamePassword,
            IncidentFeatureConfigs loginFailureUsernameMaskedPassword,
            IncidentFeatureConfigs logout,
            IncidentFeatureConfigs sessionRefreshed,
            IncidentFeatureConfigs sessionExpired,
            IncidentFeatureConfigs register1,
            IncidentFeatureConfigs register1Failure
    ) {
        var instance = new IncidentFeaturesConfigs();
        instance.login = login;
        instance.loginFailureUsernamePassword = loginFailureUsernamePassword;
        instance.loginFailureUsernameMaskedPassword = loginFailureUsernameMaskedPassword;
        instance.logout = logout;
        instance.sessionRefreshed = sessionRefreshed;
        instance.sessionExpired = sessionExpired;
        instance.register1 = register1;
        instance.register1Failure = register1Failure;
        return instance;
    }

    @Override
    public void configureRequiredIncidentsIfMissing() {
        if (isNull(this.login)) {
            this.login = new IncidentFeatureConfigs();
            this.login.configureRequiredIncidentsIfMissing();
        }
        if (isNull(this.loginFailureUsernamePassword)) {
            this.loginFailureUsernamePassword = new IncidentFeatureConfigs();
            this.loginFailureUsernamePassword.configureRequiredIncidentsIfMissing();
        }
        if (isNull(this.loginFailureUsernameMaskedPassword)) {
            this.loginFailureUsernameMaskedPassword = new IncidentFeatureConfigs();
            this.loginFailureUsernameMaskedPassword.configureRequiredIncidentsIfMissing();
        }
        if (isNull(this.logout)) {
            this.logout = new IncidentFeatureConfigs();
            this.logout.configureRequiredIncidentsIfMissing();
        }
        if (isNull(this.sessionRefreshed)) {
            this.sessionRefreshed = new IncidentFeatureConfigs();
            this.sessionRefreshed.configureRequiredIncidentsIfMissing();
        }
        if (isNull(this.sessionExpired)) {
            this.sessionExpired = new IncidentFeatureConfigs();
            this.sessionExpired.configureRequiredIncidentsIfMissing();
        }
        if (isNull(this.register1)) {
            this.register1 = new IncidentFeatureConfigs();
            this.register1.configureRequiredIncidentsIfMissing();
        }
        if (isNull(this.register1Failure)) {
            this.register1Failure = new IncidentFeatureConfigs();
            this.register1Failure.configureRequiredIncidentsIfMissing();
        }
    }
}
