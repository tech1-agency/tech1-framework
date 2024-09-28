package tech1.framework.foundation.incidents.domain.session;

import tech1.framework.foundation.domain.base.Username;
import tech1.framework.foundation.domain.geo.GeoLocation;
import tech1.framework.foundation.domain.http.requests.IPAddress;
import tech1.framework.foundation.domain.http.requests.UserAgentDetails;
import tech1.framework.foundation.domain.http.requests.UserRequestMetadata;
import org.junit.jupiter.api.Test;

import static tech1.framework.foundation.domain.tests.constants.TestsFlagsConstants.UK;
import static org.assertj.core.api.Assertions.assertThat;

class IncidentSessionExpiredTest {

    @Test
    void convertSessionExpiredIncidentTest() {
        // Arrange
        var username = Username.of("tech1");
        var incident = new IncidentSessionExpired(
                username,
                UserRequestMetadata.processed(
                        GeoLocation.processed(new IPAddress("2.2.2.2"), "UK", "UK", UK, "London"),
                        UserAgentDetails.processed("Mozilla", "MacOS", "Desktop")
                )
        );

        // Act
        var actual = incident.getPlainIncident();

        // Assert
        assertThat(actual).isNotNull();
        assertThat(actual.getType()).isEqualTo("Session Expired");
        assertThat(actual.getUsername().value()).isEqualTo("tech1");
        assertThat(actual.getAttributes())
                .hasSize(7)
                .containsOnlyKeys("incidentType", "username", "browser", "countryFlag", "ipAddress", "what", "where")
                .containsEntry("incidentType", "Session Expired")
                .containsEntry("username", username)
                .containsEntry("browser", "Mozilla")
                .containsEntry("countryFlag", UK)
                .containsEntry("ipAddress", "2.2.2.2")
                .containsEntry("what", "Mozilla, MacOS on Desktop")
                .containsEntry("where", "🇬🇧 UK, London");
    }
}
