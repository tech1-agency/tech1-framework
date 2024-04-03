package io.tech1.framework.incidents.domain.authetication;

import io.tech1.framework.domain.base.Password;
import io.tech1.framework.domain.base.Username;
import io.tech1.framework.domain.base.UsernamePasswordCredentials;
import io.tech1.framework.domain.http.requests.UserRequestMetadata;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class IncidentAuthenticationLoginFailureUsernamePasswordTest {

    @Test
    void convertAuthenticationLoginFailureUsernamePasswordIncidentTest() {
        // Arrange
        var incident = new IncidentAuthenticationLoginFailureUsernamePassword(
                UsernamePasswordCredentials.testsHardcoded(),
                UserRequestMetadata.valid()
        );

        // Act
        var actual = incident.getPlainIncident();

        // Assert
        assertThat(actual).isNotNull();
        assertThat(actual.getType()).isEqualTo("Authentication Login Failure Username/Password");
        assertThat(actual.getUsername().value()).isEqualTo("tech1");
        assertThat(actual.getAttributes()).hasSize(3);
        assertThat(actual.getAttributes()).containsOnlyKeys("incidentType", "username", "password");
        assertThat(actual.getAttributes()).containsEntry("incidentType", "Authentication Login Failure Username/Password");
        assertThat(actual.getAttributes()).containsEntry("username", Username.testsHardcoded());
        assertThat(actual.getAttributes()).containsEntry("password", Password.testsHardcoded());
    }
}
