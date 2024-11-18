package jbst.foundation.incidents.domain.authetication;

import jbst.foundation.domain.base.Username;
import jbst.foundation.domain.geo.GeoLocation;
import jbst.foundation.domain.http.requests.IPAddress;
import jbst.foundation.domain.http.requests.UserAgentDetails;
import jbst.foundation.domain.http.requests.UserRequestMetadata;
import org.junit.jupiter.api.Test;

import static jbst.foundation.domain.tests.constants.TestsFlagsConstants.UK;
import static org.assertj.core.api.Assertions.assertThat;

class IncidentAuthenticationLogoutFullTest {

    @Test
    void convertAuthenticationLogoutFullIncidentTest() {
        // Arrange
        var username = Username.hardcoded();
        var incident = new IncidentAuthenticationLogoutFull(
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
        assertThat(actual.getType()).isEqualTo("Authentication Logout");
        assertThat(actual.getUsername().value()).isEqualTo("jbst");
        assertThat(actual.getAttributes())
                .hasSize(7)
                .containsOnlyKeys("incidentType", "username", "browser", "countryFlag", "ipAddress", "what", "where")
                .containsEntry("incidentType", "Authentication Logout")
                .containsEntry("username", username)
                .containsEntry("browser", "Mozilla")
                .containsEntry("countryFlag", UK)
                .containsEntry("ipAddress", "2.2.2.2")
                .containsEntry("what", "Mozilla, MacOS on Desktop")
                .containsEntry("where", "🇬🇧 UK, London");
    }
}
