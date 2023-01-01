package io.tech1.framework.b2b.mongodb.security.jwt.incidents.converters.impl;

import io.tech1.framework.b2b.mongodb.security.jwt.incidents.converters.SecurityJwtIncidentConverter;
import io.tech1.framework.domain.base.Password;
import io.tech1.framework.domain.base.Username;
import io.tech1.framework.domain.geo.GeoLocation;
import io.tech1.framework.domain.http.requests.IPAddress;
import io.tech1.framework.domain.http.requests.UserAgentDetails;
import io.tech1.framework.domain.http.requests.UserRequestMetadata;
import io.tech1.framework.incidents.domain.authetication.*;
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

import static io.tech1.framework.domain.constants.StringConstants.NO_FLAG;
import static io.tech1.framework.domain.tests.constants.TestsConstants.FLAG_UK;
import static io.tech1.framework.domain.utilities.random.RandomUtility.randomString;
import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith({ SpringExtension.class })
@ContextConfiguration(loader= AnnotationConfigContextLoader.class)
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class SecurityJwtIncidentConverterImplTest {

    @Configuration
    static class ContextConfiguration {
        @Bean
        SecurityJwtIncidentConverter securityJwtIncidentConverter() {
            return new SecurityJwtIncidentConverterImpl();
        }
    }

    private final SecurityJwtIncidentConverter componentUnderTest;

    @Test
    public void convertAuthenticationLoginIncidentExceptionTest() {
        // Arrange
        var username = Username.of("tech1");
        var incident = IncidentAuthenticationLogin.of(
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
        assertThat(actual.getUsername().getIdentifier()).isEqualTo("tech1");
        assertThat(actual.getAttributes()).hasSize(8);
        assertThat(actual.getAttributes()).containsOnlyKeys("incidentType", "username", "browser", "countryFlag", "ipAddress", "what", "where", "exception");
        assertThat(actual.getAttributes().get("incidentType")).isEqualTo("Authentication Login");
        assertThat(actual.getAttributes().get("username")).isEqualTo(username);
        assertThat(actual.getAttributes().get("browser")).isEqualTo("[?]");
        assertThat(actual.getAttributes().get("countryFlag")).isEqualTo(NO_FLAG);
        assertThat(actual.getAttributes().get("ipAddress")).isEqualTo("8.8.8.8");
        assertThat(actual.getAttributes().get("what")).isEqualTo("[?], [?] on [?]");
        assertThat(actual.getAttributes().get("where")).isEqualTo("Unknown, Unknown");
        assertThat(actual.getAttributes().get("exception")).isEqualTo("exception1");
    }

    @Test
    public void convertAuthenticationLoginIncidentTest() {
        // Arrange
        var username = Username.of("tech1");
        var incident = IncidentAuthenticationLogin.of(
                username,
                UserRequestMetadata.processing(new IPAddress("127.0.0.1"))
        );

        // Act
        var actual = this.componentUnderTest.convert(incident);

        // Assert
        assertThat(actual).isNotNull();
        assertThat(actual.getType()).isEqualTo("Authentication Login");
        assertThat(actual.getUsername().getIdentifier()).isEqualTo("tech1");
        assertThat(actual.getAttributes()).hasSize(7);
        assertThat(actual.getAttributes()).containsOnlyKeys("incidentType", "username", "browser", "countryFlag", "ipAddress", "what", "where");
        assertThat(actual.getAttributes().get("incidentType")).isEqualTo("Authentication Login");
        assertThat(actual.getAttributes().get("username")).isEqualTo(username);
        assertThat(actual.getAttributes().get("browser")).isEqualTo("[?]");
        assertThat(actual.getAttributes().get("countryFlag")).isEqualTo(NO_FLAG);
        assertThat(actual.getAttributes().get("ipAddress")).isEqualTo("127.0.0.1");
        assertThat(actual.getAttributes().get("what")).isEqualTo("—");
        assertThat(actual.getAttributes().get("where")).isEqualTo("Processing...Please wait!");
    }

    @Test
    public void convertAuthenticationLoginFailureUsernamePasswordIncidentTest() {
        // Arrange
        var username = Username.of("tech1");
        var password = Password.of("passwordTOP123!");
        var incident = IncidentAuthenticationLoginFailureUsernamePassword.of(
                username,
                password
        );

        // Act
        var actual = this.componentUnderTest.convert(incident);

        // Assert
        assertThat(actual).isNotNull();
        assertThat(actual.getType()).isEqualTo("Authentication Login Failure Username/Password");
        assertThat(actual.getUsername().getIdentifier()).isEqualTo("tech1");
        assertThat(actual.getAttributes()).hasSize(3);
        assertThat(actual.getAttributes()).containsOnlyKeys("incidentType", "username", "password");
        assertThat(actual.getAttributes().get("incidentType")).isEqualTo("Authentication Login Failure Username/Password");
        assertThat(actual.getAttributes().get("username")).isEqualTo(username);
        assertThat(actual.getAttributes().get("password")).isEqualTo(password);
    }

    @Test
    public void convertAuthenticationLoginFailureUsernameMaskedPasswordIncidentTest() {
        // Arrange
        var username = Username.of("tech1");
        var password = Password.of("passwordTOP123!");
        var incident = IncidentAuthenticationLoginFailureUsernameMaskedPassword.of(
                username,
                password
        );

        // Act
        var actual = this.componentUnderTest.convert(incident);

        // Assert
        assertThat(actual).isNotNull();
        assertThat(actual.getType()).isEqualTo("Authentication Login Failure Username/Masked Password");
        assertThat(actual.getUsername().getIdentifier()).isEqualTo("tech1");
        assertThat(actual.getAttributes()).hasSize(3);
        assertThat(actual.getAttributes()).containsOnlyKeys("incidentType", "username", "password");
        assertThat(actual.getAttributes().get("incidentType")).isEqualTo("Authentication Login Failure Username/Masked Password");
        assertThat(actual.getAttributes().get("username")).isEqualTo(username);
        assertThat(actual.getAttributes().get("password")).isEqualTo(Password.of("passw**********"));
    }

    @Test
    public void convertAuthenticationLogoutMinIncidentTest() {
        // Arrange
        var username = Username.of("tech1");
        var incident = IncidentAuthenticationLogoutMin.of(
                username
        );

        // Act
        var actual = this.componentUnderTest.convert(incident);

        // Assert
        assertThat(actual).isNotNull();
        assertThat(actual.getType()).isEqualTo("Authentication Logout Min");
        assertThat(actual.getUsername().getIdentifier()).isEqualTo("tech1");
        assertThat(actual.getAttributes()).hasSize(2);
        assertThat(actual.getAttributes()).containsOnlyKeys("incidentType", "username");
        assertThat(actual.getAttributes().get("incidentType")).isEqualTo("Authentication Logout Min");
        assertThat(actual.getAttributes().get("username")).isEqualTo(username);
    }

    @Test
    public void convertAuthenticationLogoutFullIncidentTest() {
        // Arrange
        var username = Username.of("tech1");
        var incident = IncidentAuthenticationLogoutFull.of(
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
        assertThat(actual.getUsername().getIdentifier()).isEqualTo("tech1");
        assertThat(actual.getAttributes()).hasSize(7);
        assertThat(actual.getAttributes()).containsOnlyKeys("incidentType", "username", "browser", "countryFlag", "ipAddress", "what", "where");
        assertThat(actual.getAttributes().get("incidentType")).isEqualTo("Authentication Logout");
        assertThat(actual.getAttributes().get("username")).isEqualTo(username);
        assertThat(actual.getAttributes().get("browser")).isEqualTo("Mozilla");
        assertThat(actual.getAttributes().get("countryFlag")).isEqualTo(FLAG_UK);
        assertThat(actual.getAttributes().get("ipAddress")).isEqualTo("2.2.2.2");
        assertThat(actual.getAttributes().get("what")).isEqualTo("Mozilla, MacOS on Desktop");
        assertThat(actual.getAttributes().get("where")).isEqualTo("UK, London");
    }

    @Test
    public void convertSessionRefreshedIncidentTest() {
        // Arrange
        var username = Username.of("tech1");
        var incident = IncidentSessionRefreshed.of(
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
        assertThat(actual.getUsername().getIdentifier()).isEqualTo("tech1");
        assertThat(actual.getAttributes()).hasSize(7);
        assertThat(actual.getAttributes()).containsOnlyKeys("incidentType", "username", "browser", "countryFlag", "ipAddress", "what", "where");
        assertThat(actual.getAttributes().get("incidentType")).isEqualTo("Session Refreshed");
        assertThat(actual.getAttributes().get("username")).isEqualTo(username);
        assertThat(actual.getAttributes().get("browser")).isEqualTo("Mozilla");
        assertThat(actual.getAttributes().get("countryFlag")).isEqualTo(FLAG_UK);
        assertThat(actual.getAttributes().get("ipAddress")).isEqualTo("2.2.2.2");
        assertThat(actual.getAttributes().get("what")).isEqualTo("Mozilla, MacOS on Desktop");
        assertThat(actual.getAttributes().get("where")).isEqualTo("UK, London");
    }

    @Test
    public void convertSessionExpiredIncidentTest() {
        // Arrange
        var username = Username.of("tech1");
        var incident = IncidentSessionExpired.of(
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
        assertThat(actual.getUsername().getIdentifier()).isEqualTo("tech1");
        assertThat(actual.getAttributes()).hasSize(7);
        assertThat(actual.getAttributes()).containsOnlyKeys("incidentType", "username", "browser", "countryFlag", "ipAddress", "what", "where");
        assertThat(actual.getAttributes().get("incidentType")).isEqualTo("Session Expired");
        assertThat(actual.getAttributes().get("username")).isEqualTo(username);
        assertThat(actual.getAttributes().get("browser")).isEqualTo("Mozilla");
        assertThat(actual.getAttributes().get("countryFlag")).isEqualTo(FLAG_UK);
        assertThat(actual.getAttributes().get("ipAddress")).isEqualTo("2.2.2.2");
        assertThat(actual.getAttributes().get("what")).isEqualTo("Mozilla, MacOS on Desktop");
        assertThat(actual.getAttributes().get("where")).isEqualTo("UK, London");
    }

    @Test
    public void convertRegister1IncidentTest() {
        // Arrange
        var username = Username.of("tech1");
        var incident = IncidentRegistration1.of(
                username
        );

        // Act
        var actual = this.componentUnderTest.convert(incident);

        // Assert
        assertThat(actual).isNotNull();
        assertThat(actual.getType()).isEqualTo("Register1");
        assertThat(actual.getUsername().getIdentifier()).isEqualTo("tech1");
        assertThat(actual.getAttributes()).hasSize(2);
        assertThat(actual.getAttributes()).containsOnlyKeys("incidentType", "username");
        assertThat(actual.getAttributes().get("incidentType")).isEqualTo("Register1");
        assertThat(actual.getAttributes().get("username")).isEqualTo(username);
    }

    @Test
    public void convertRegister1FailureIncidentTest() {
        // Arrange
        var username = Username.of("tech1");
        var exception = randomString();
        var invitationCode = randomString();
        var incident = IncidentRegistration1Failure.of(
                username,
                exception,
                invitationCode
        );

        // Act
        var actual = this.componentUnderTest.convert(incident);

        // Assert
        assertThat(actual).isNotNull();
        assertThat(actual.getType()).isEqualTo("Register1 Failure");
        assertThat(actual.getUsername().getIdentifier()).isEqualTo("tech1");
        assertThat(actual.getAttributes()).hasSize(4);
        assertThat(actual.getAttributes()).containsOnlyKeys("incidentType", "username", "exception", "invitationCode");
        assertThat(actual.getAttributes().get("incidentType")).isEqualTo("Register1 Failure");
        assertThat(actual.getAttributes().get("username")).isEqualTo(username);
        assertThat(actual.getAttributes().get("exception")).isEqualTo(exception);
        assertThat(actual.getAttributes().get("invitationCode")).isEqualTo(invitationCode);
    }
}
