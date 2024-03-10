package io.tech1.framework.b2b.base.security.jwt.incidents.converters.impl;

import io.tech1.framework.b2b.base.security.jwt.incidents.converters.SecurityJwtIncidentConverter;
import io.tech1.framework.domain.base.Username;
import io.tech1.framework.domain.geo.GeoLocation;
import io.tech1.framework.domain.http.requests.IPAddress;
import io.tech1.framework.domain.http.requests.UserAgentDetails;
import io.tech1.framework.domain.http.requests.UserRequestMetadata;
import io.tech1.framework.incidents.converters.IncidentConverter;
import io.tech1.framework.incidents.converters.impl.IncidentConverterImpl;
import io.tech1.framework.incidents.domain.authetication.IncidentAuthenticationLogin;
import io.tech1.framework.incidents.domain.authetication.IncidentAuthenticationLogoutFull;
import io.tech1.framework.incidents.domain.registration.IncidentRegistration1;
import io.tech1.framework.incidents.domain.registration.IncidentRegistration1Failure;
import io.tech1.framework.incidents.domain.session.IncidentSessionExpired;
import io.tech1.framework.incidents.domain.session.IncidentSessionRefreshed;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.context.support.AnnotationConfigContextLoader;

import static io.tech1.framework.domain.tests.constants.TestsFlagsConstants.FLAG_UK;
import static io.tech1.framework.domain.tests.constants.TestsFlagsConstants.FLAG_UNKNOWN;
import static io.tech1.framework.domain.utilities.random.RandomUtility.randomString;
import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith({ SpringExtension.class })
@ContextConfiguration(loader= AnnotationConfigContextLoader.class)
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
class SecurityJwtIncidentConverterImplTest {

    @Configuration
    static class ContextConfiguration {
        @Bean
        IncidentConverter incidentConverter() {
            return new IncidentConverterImpl();
        }

        @Bean
        SecurityJwtIncidentConverter securityJwtIncidentConverter() {
            return new SecurityJwtIncidentConverterImpl(
                    this.incidentConverter()
            );
        }
    }

    private final SecurityJwtIncidentConverter componentUnderTest;

    @Test
    void convertAuthenticationLoginIncidentExceptionTest() {
        // Arrange
        var username = Username.of("tech1");
        var incident = new IncidentAuthenticationLogin(
                username,
                UserRequestMetadata.processed(
                        GeoLocation.unknown(new IPAddress("8.8.8.8"), "exception1"),
                        UserAgentDetails.processing()
                )
        );

        // Act
        var actual = this.componentUnderTest.convert(incident);

        // Assert
        assertThat(actual).isNotNull();
        assertThat(actual.getType()).isEqualTo("Authentication Login");
        assertThat(actual.getUsername().value()).isEqualTo("tech1");
        assertThat(actual.getAttributes())
                .hasSize(8)
                .containsOnlyKeys("incidentType", "username", "browser", "countryFlag", "ipAddress", "what", "where", "exception")
                .containsEntry("incidentType", "Authentication Login")
                .containsEntry("username", username)
                .containsEntry("browser", "[?]")
                .containsEntry("countryFlag", FLAG_UNKNOWN)
                .containsEntry("ipAddress", "8.8.8.8")
                .containsEntry("what", "[?], [?] on [?]")
                .containsEntry("where", "Unknown, Unknown")
                .containsEntry("exception", "exception1");
    }

    @Test
    void convertAuthenticationLoginIncidentTest() {
        // Arrange
        var username = Username.of("tech1");
        var incident = new IncidentAuthenticationLogin(
                username,
                UserRequestMetadata.processing(new IPAddress("127.0.0.1"))
        );

        // Act
        var actual = this.componentUnderTest.convert(incident);

        // Assert
        assertThat(actual).isNotNull();
        assertThat(actual.getType()).isEqualTo("Authentication Login");
        assertThat(actual.getUsername().value()).isEqualTo("tech1");
        assertThat(actual.getAttributes())
                .hasSize(7)
                .containsOnlyKeys("incidentType", "username", "browser", "countryFlag", "ipAddress", "what", "where")
                .containsEntry("incidentType", "Authentication Login")
                .containsEntry("username", username)
                .containsEntry("browser", "[?]")
                .containsEntry("countryFlag", FLAG_UNKNOWN)
                .containsEntry("ipAddress", "127.0.0.1")
                .containsEntry("what", "—")
                .containsEntry("where", "Processing...Please wait!");
    }

    @Test
    void convertAuthenticationLogoutFullIncidentTest() {
        // Arrange
        var username = Username.of("tech1");
        var incident = new IncidentAuthenticationLogoutFull(
                username,
                UserRequestMetadata.processed(
                        GeoLocation.processed(new IPAddress("2.2.2.2"), "UK", "UK", FLAG_UK, "London"),
                        UserAgentDetails.processed("Mozilla", "MacOS", "Desktop")
                )
        );

        // Act
        var actual = this.componentUnderTest.convert(incident);

        // Assert
        assertThat(actual).isNotNull();
        assertThat(actual.getType()).isEqualTo("Authentication Logout");
        assertThat(actual.getUsername().value()).isEqualTo("tech1");
        assertThat(actual.getAttributes())
                .hasSize(7)
                .containsOnlyKeys("incidentType", "username", "browser", "countryFlag", "ipAddress", "what", "where")
                .containsEntry("incidentType", "Authentication Logout")
                .containsEntry("username", username)
                .containsEntry("browser", "Mozilla")
                .containsEntry("countryFlag", FLAG_UK)
                .containsEntry("ipAddress", "2.2.2.2")
                .containsEntry("what", "Mozilla, MacOS on Desktop")
                .containsEntry("where", "UK, London");
    }

    @Test
    void convertSessionRefreshedIncidentTest() {
        // Arrange
        var username = Username.of("tech1");
        var incident = new IncidentSessionRefreshed(
                username,
                UserRequestMetadata.processed(
                        GeoLocation.processed(new IPAddress("2.2.2.2"), "UK", "UK", FLAG_UK, "London"),
                        UserAgentDetails.processed("Mozilla", "MacOS", "Desktop")
                )
        );

        // Act
        var actual = this.componentUnderTest.convert(incident);

        // Assert
        assertThat(actual).isNotNull();
        assertThat(actual.getType()).isEqualTo("Session Refreshed");
        assertThat(actual.getUsername().value()).isEqualTo("tech1");
        assertThat(actual.getAttributes())
                .hasSize(7)
                .containsOnlyKeys("incidentType", "username", "browser", "countryFlag", "ipAddress", "what", "where")
                .containsEntry("incidentType", "Session Refreshed")
                .containsEntry("username", username)
                .containsEntry("browser", "Mozilla")
                .containsEntry("countryFlag", FLAG_UK)
                .containsEntry("ipAddress", "2.2.2.2")
                .containsEntry("what", "Mozilla, MacOS on Desktop")
                .containsEntry("where", "UK, London");
    }

    @Test
    void convertSessionExpiredIncidentTest() {
        // Arrange
        var username = Username.of("tech1");
        var incident = new IncidentSessionExpired(
                username,
                UserRequestMetadata.processed(
                        GeoLocation.processed(new IPAddress("2.2.2.2"), "UK", "UK", FLAG_UK, "London"),
                        UserAgentDetails.processed("Mozilla", "MacOS", "Desktop")
                )
        );

        // Act
        var actual = this.componentUnderTest.convert(incident);

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
                .containsEntry("countryFlag", FLAG_UK)
                .containsEntry("ipAddress", "2.2.2.2")
                .containsEntry("what", "Mozilla, MacOS on Desktop")
                .containsEntry("where", "UK, London");
    }

    @Test
    void convertRegister1IncidentTest() {
        // Arrange
        var username = Username.of("tech1");
        var incident = new IncidentRegistration1(
                username
        );

        // Act
        var actual = this.componentUnderTest.convert(incident);

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
        var actual = this.componentUnderTest.convert(incident);

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
