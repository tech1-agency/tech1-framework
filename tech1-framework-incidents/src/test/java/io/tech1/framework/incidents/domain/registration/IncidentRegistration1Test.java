package io.tech1.framework.incidents.domain.registration;

import io.tech1.framework.foundation.domain.base.Username;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class IncidentRegistration1Test {

    @Test
    void convertRegister1IncidentTest() {
        // Arrange
        var username = Username.of("tech1");
        var incident = new IncidentRegistration1(
                username
        );

        // Act
        var actual = incident.getPlainIncident();

        // Assert
        assertThat(actual).isNotNull();
        assertThat(actual.getType()).isEqualTo("Register1");
        assertThat(actual.getUsername().value()).isEqualTo("tech1");
        assertThat(actual.getAttributes())
                .hasSize(2)
                .containsOnlyKeys("incidentType", "username")
                .containsEntry("incidentType", "Register1")
                .containsEntry("username", username);
    }
}
