package io.tech1.framework.domain.properties.configs;

import io.tech1.framework.domain.properties.base.RemoteServer;
import io.tech1.framework.domain.properties.configs.incidents.IncidentFeaturesConfigs;
import io.tech1.framework.domain.utilities.random.EntityUtility;
import org.junit.jupiter.api.Test;

import static io.tech1.framework.domain.properties.configs.incidents.IncidentFeatureConfigs.disabledIncidentFeatureConfigs;
import static io.tech1.framework.domain.properties.configs.incidents.IncidentFeatureConfigs.enabledIncidentFeatureConfigs;
import static io.tech1.framework.domain.utilities.reflections.ReflectionUtility.getGetters;
import static org.assertj.core.api.Assertions.assertThat;

public class IncidentConfigsTest {

    @Test
    public void constructorTest() {
        // Act
        var incidentConfigs = new IncidentConfigs();

        // Assert
        assertThat(incidentConfigs.isEnabled()).isFalse();
        assertThat(incidentConfigs.getRemoteServer()).isNull();
        assertThat(incidentConfigs.getFeatures()).isNull();
    }

    @Test
    public void disabledTest() {
        // Act
        var incidentConfigs = IncidentConfigs.disabled();

        // Assert
        assertThat(incidentConfigs.isEnabled()).isFalse();
        assertThat(incidentConfigs.getRemoteServer()).isNull();
        assertThat(incidentConfigs.getFeatures()).isNull();
    }

    @Test
    public void ofFeaturesNullTest() {
        // Act
        var incidentConfigs = IncidentConfigs.of(
                true,
                EntityUtility.entity(RemoteServer.class),
                null
        );

        // Assert
        assertThat(incidentConfigs.isEnabled()).isTrue();
        assertThat(incidentConfigs.getRemoteServer()).isNotNull();
        assertThat(incidentConfigs.getFeatures()).isNull();
    }

    @Test
    public void ofFeaturesConstructorTest() {
        // Act
        var incidentConfigs = IncidentConfigs.of(
                true,
                EntityUtility.entity(RemoteServer.class),
                new IncidentFeaturesConfigs()
        );

        // Assert
        assertThat(incidentConfigs.isEnabled()).isTrue();
        assertThat(incidentConfigs.getRemoteServer()).isNotNull();
        var incidentFeaturesConfigs = incidentConfigs.getFeatures();
        assertThat(getGetters(incidentFeaturesConfigs)).hasSize(8);
        assertThat(incidentFeaturesConfigs.getLogin()).isNull();
        assertThat(incidentFeaturesConfigs.getLoginFailureUsernamePassword()).isNull();
        assertThat(incidentFeaturesConfigs.getLoginFailureUsernameMaskedPassword()).isNull();
        assertThat(incidentFeaturesConfigs.getLogout()).isNull();
        assertThat(incidentFeaturesConfigs.getSessionRefreshed()).isNull();
        assertThat(incidentFeaturesConfigs.getSessionExpired()).isNull();
        assertThat(incidentFeaturesConfigs.getRegister1()).isNull();
        assertThat(incidentFeaturesConfigs.getRegister1Failure()).isNull();
    }

    @Test
    public void notConfigureRequiredIncidentsTest() {
        // Arrange
        var incidentConfigs = IncidentConfigs.of(
                true,
                EntityUtility.entity(RemoteServer.class),
                IncidentFeaturesConfigs.of(
                        enabledIncidentFeatureConfigs(),
                        enabledIncidentFeatureConfigs(),
                        disabledIncidentFeatureConfigs(),
                        enabledIncidentFeatureConfigs(),
                        enabledIncidentFeatureConfigs(),
                        enabledIncidentFeatureConfigs(),
                        enabledIncidentFeatureConfigs(),
                        enabledIncidentFeatureConfigs(),
                        enabledIncidentFeatureConfigs()
                )
        );

        // Act
        incidentConfigs.assertProperties();

        // Assert
        assertThat(incidentConfigs.isEnabled()).isTrue();
        assertThat(incidentConfigs.getRemoteServer()).isNotNull();
        var incidentFeaturesConfigs = incidentConfigs.getFeatures();
        assertThat(incidentFeaturesConfigs).isNotNull();
        assertThat(getGetters(incidentFeaturesConfigs)).hasSize(8);
        assertThat(incidentFeaturesConfigs.getLogin()).isNotNull();
        assertThat(incidentFeaturesConfigs.getLogin().isEnabled()).isTrue();

        assertThat(incidentFeaturesConfigs.getLoginFailureUsernamePassword()).isNotNull();
        assertThat(incidentFeaturesConfigs.getLoginFailureUsernamePassword().isEnabled()).isTrue();

        assertThat(incidentFeaturesConfigs.getLoginFailureUsernameMaskedPassword()).isNotNull();
        assertThat(incidentFeaturesConfigs.getLoginFailureUsernameMaskedPassword().isEnabled()).isFalse();

        assertThat(incidentFeaturesConfigs.getLogout()).isNotNull();
        assertThat(incidentFeaturesConfigs.getLogout().isEnabled()).isTrue();

        assertThat(incidentFeaturesConfigs.getLogoutMin()).isNotNull();
        assertThat(incidentFeaturesConfigs.getLogoutMin().isEnabled()).isTrue();

        assertThat(incidentFeaturesConfigs.getSessionRefreshed()).isNotNull();
        assertThat(incidentFeaturesConfigs.getSessionRefreshed().isEnabled()).isTrue();

        assertThat(incidentFeaturesConfigs.getSessionExpired()).isNotNull();
        assertThat(incidentFeaturesConfigs.getSessionExpired().isEnabled()).isTrue();

        assertThat(incidentFeaturesConfigs.getRegister1()).isNotNull();
        assertThat(incidentFeaturesConfigs.getRegister1().isEnabled()).isTrue();

        assertThat(incidentFeaturesConfigs.getRegister1Failure()).isNotNull();
        assertThat(incidentFeaturesConfigs.getRegister1Failure().isEnabled()).isTrue();
    }
}
