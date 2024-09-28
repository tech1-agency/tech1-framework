package tech1.framework.foundation.incidents.domain.authetication;

import tech1.framework.foundation.domain.base.UsernamePasswordCredentials;
import tech1.framework.foundation.domain.http.requests.UserRequestMetadata;
import org.junit.jupiter.api.Test;

import static tech1.framework.foundation.domain.tests.constants.TestsFlagsConstants.UKRAINE;
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
        assertThat(actual.getAttributes())
                .hasSize(8)
                .containsOnlyKeys("incidentType", "username", "password", "browser", "countryFlag", "ipAddress", "what", "where")
                .containsEntry("incidentType", "Authentication Login Failure Username/Password")
                .containsEntry("username", incident.credentials().username())
                .containsEntry("password", incident.credentials().password())
                .containsEntry("browser", "Chrome")
                .containsEntry("countryFlag", UKRAINE)
                .containsEntry("ipAddress", "127.0.0.1")
                .containsEntry("what", "Chrome, macOS on Desktop")
                .containsEntry("where", "🇺🇦 Ukraine, Lviv");
    }
}
