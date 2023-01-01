package io.tech1.framework.domain.properties.configs.incidents;

import io.tech1.framework.domain.properties.annotations.MandatoryProperty;
import io.tech1.framework.domain.properties.configs.AbstractPropertiesConfigs;
import lombok.Data;
import lombok.EqualsAndHashCode;

// Lombok (property-based)
@Data
@EqualsAndHashCode(callSuper = true)
public class IncidentFeaturesConfigs extends AbstractPropertiesConfigs {
    @MandatoryProperty
    private IncidentFeatureConfigs login;
    @MandatoryProperty
    private IncidentFeatureConfigs loginFailureUsernamePassword;
    @MandatoryProperty
    private IncidentFeatureConfigs loginFailureUsernameMaskedPassword;
    @MandatoryProperty
    private IncidentFeatureConfigs logout;
    @MandatoryProperty
    private IncidentFeatureConfigs logoutMin;
    @MandatoryProperty
    private IncidentFeatureConfigs sessionRefreshed;
    @MandatoryProperty
    private IncidentFeatureConfigs sessionExpired;
    @MandatoryProperty
    private IncidentFeatureConfigs register1;
    @MandatoryProperty
    private IncidentFeatureConfigs register1Failure;

    // NOTE: test-purposes
    public static IncidentFeaturesConfigs of(
            IncidentFeatureConfigs login,
            IncidentFeatureConfigs loginFailureUsernamePassword,
            IncidentFeatureConfigs loginFailureUsernameMaskedPassword,
            IncidentFeatureConfigs logout,
            IncidentFeatureConfigs logoutMin,
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
        instance.logoutMin = logoutMin;
        instance.sessionRefreshed = sessionRefreshed;
        instance.sessionExpired = sessionExpired;
        instance.register1 = register1;
        instance.register1Failure = register1Failure;
        return instance;
    }
}
