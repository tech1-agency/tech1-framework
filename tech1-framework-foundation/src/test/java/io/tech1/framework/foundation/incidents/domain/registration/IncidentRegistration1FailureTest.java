package io.tech1.framework.foundation.incidents.domain.registration;

import io.tech1.framework.foundation.domain.base.Username;
import io.tech1.framework.foundation.incidents.domain.registration.IncidentRegistration1Failure;
import org.junit.jupiter.api.Test;

import static io.tech1.framework.foundation.utilities.random.RandomUtility.randomString;
import static org.assertj.core.api.Assertions.assertThat;

class IncidentRegistration1FailureTest {

    @Test
    void convertRegister1FailureIncidentTest() {
        // Arrange
        var username = Username.of("tech1");
        var exception = randomString();
        var invitationCode = randomString();
        var incident = IncidentRegistration1Failure.of(
                username,
                invitationCode,
                exception
        );

        // Act
        var actual = incident.getPlainIncident();

        // Assert
        assertThat(actual).isNotNull();
        assertThat(actual.getType()).isEqualTo("Register1 Failure");
        assertThat(actual.getUsername().value()).isEqualTo("tech1");
        assertThat(actual.getAttributes())
                .hasSize(5)
                .containsOnlyKeys("incidentType", "username", "exception", "invitationCode", "invitationCodeOwner")
                .containsEntry("incidentType", "Register1 Failure")
                .containsEntry("username", username)
                .containsEntry("exception", exception)
                .containsEntry("invitationCode", invitationCode)
                .containsEntry("invitationCodeOwner", Username.of("—"));
    }
}
