package io.tech1.framework.foundation.incidents.domain.authetication;

import io.tech1.framework.foundation.domain.base.Username;
import io.tech1.framework.foundation.incidents.domain.authetication.IncidentAuthenticationLogoutMin;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class IncidentAuthenticationLogoutMinTest {

    @Test
    void convertAuthenticationLogoutMinIncidentTest() {
        // Arrange
        var username = Username.of("tech1");
        var incident = new IncidentAuthenticationLogoutMin(
                username
        );

        // Act
        var actual = incident.getPlainIncident();

        // Assert
        assertThat(actual).isNotNull();
        assertThat(actual.getType()).isEqualTo("Authentication Logout Min");
        assertThat(actual.getUsername().value()).isEqualTo("tech1");
        assertThat(actual.getAttributes()).hasSize(2);
        assertThat(actual.getAttributes()).containsOnlyKeys("incidentType", "username");
        assertThat(actual.getAttributes()).containsEntry("incidentType", "Authentication Logout Min");
        assertThat(actual.getAttributes()).containsEntry("username", username);
    }
}
