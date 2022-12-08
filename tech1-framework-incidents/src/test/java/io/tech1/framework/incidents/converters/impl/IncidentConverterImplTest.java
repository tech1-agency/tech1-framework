package io.tech1.framework.incidents.converters.impl;

import io.tech1.framework.domain.base.Password;
import io.tech1.framework.domain.base.Username;
import io.tech1.framework.domain.geo.GeoLocation;
import io.tech1.framework.domain.http.requests.IPAddress;
import io.tech1.framework.domain.http.requests.UserAgentDetails;
import io.tech1.framework.domain.http.requests.UserRequestMetadata;
import io.tech1.framework.incidents.converters.IncidentConverter;
import io.tech1.framework.incidents.domain.authetication.*;
import io.tech1.framework.incidents.domain.registration.IncidentRegistration1Failure;
import io.tech1.framework.incidents.domain.registration.IncidentRegistration1;
import io.tech1.framework.incidents.domain.session.IncidentSessionExpired;
import io.tech1.framework.incidents.domain.session.IncidentSessionRefreshed;
import io.tech1.framework.incidents.domain.throwable.IncidentThrowable;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.context.support.AnnotationConfigContextLoader;

import java.util.List;
import java.util.Map;

import static io.tech1.framework.domain.utilities.random.RandomUtility.randomMethod;
import static io.tech1.framework.domain.utilities.random.RandomUtility.randomString;
import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith({ SpringExtension.class })
@ContextConfiguration(loader= AnnotationConfigContextLoader.class)
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class IncidentConverterImplTest {

    @Configuration
    static class ContextConfiguration {
        @Bean
        IncidentConverter asyncUncaughtExceptionHandlerPublisher() {
            return new IncidentConverterImpl();
        }
    }

    private final IncidentConverter componentUnderTest;

    @Test
    public void convertThrowableIncident1Test() {
        // Arrange
        var throwable = new NullPointerException("Tech1");
        var throwableIncident = IncidentThrowable.of(throwable);

        // Act
        var actual = this.componentUnderTest.convert(throwableIncident);

        // Assert
        assertThat(actual).isNotNull();
        assertThat(actual.getType()).isEqualTo("Throwable");
        assertThat(actual.getUsername().getIdentifier()).isEqualTo("Unknown");
        assertThat(actual.getAttributes()).hasSize(4);
        assertThat(actual.getAttributes()).containsOnlyKeys("incidentType", "Exception", "Message", "Trace");
        assertThat(actual.getAttributes().get("incidentType")).isEqualTo("Throwable");
        assertThat(actual.getAttributes().get("Exception")).isEqualTo(NullPointerException.class);
        assertThat(actual.getAttributes().get("Message")).isEqualTo("Tech1");
        assertThat(actual.getAttributes().get("Trace").toString()).startsWith("Throwable occurred! Please take required actions!");
    }

    @Test
    public void convertThrowableIncident2Test() {
        // Arrange
        var object = new Object();
        var throwable = new NullPointerException("Tech1");
        var method = randomMethod();
        var params = List.of(object, "param1", 1L);
        var throwableIncident = IncidentThrowable.of(throwable, method, params);

        // Act
        var actual = this.componentUnderTest.convert(throwableIncident);

        // Assert
        assertThat(actual).isNotNull();
        assertThat(actual.getType()).isEqualTo("Throwable");
        assertThat(actual.getUsername().getIdentifier()).isEqualTo("Unknown");
        assertThat(actual.getAttributes()).hasSize(6);
        assertThat(actual.getAttributes()).containsOnlyKeys("incidentType", "Exception", "Message", "Trace", "Method", "Params");
        assertThat(actual.getAttributes().get("incidentType")).isEqualTo("Throwable");
        assertThat(actual.getAttributes().get("Exception")).isEqualTo(NullPointerException.class);
        assertThat(actual.getAttributes().get("Message")).isEqualTo("Tech1");
        assertThat(actual.getAttributes().get("Trace").toString()).startsWith("Throwable occurred! Please take required actions!");
        assertThat(actual.getAttributes().get("Method").toString()).contains("protected void java.lang.Object.finalize() throws java.lang.Throwable");
        assertThat(actual.getAttributes().get("Params")).isEqualTo(object + ", param1, 1");
    }

    @Test
    public void convertThrowableIncident3Test() {
        // Arrange
        var object = new Object();
        var throwable = new NullPointerException("Tech1");
        var attributes = Map.of("Key1", object);
        var throwableIncident = IncidentThrowable.of(throwable, attributes);

        // Act
        var actual = this.componentUnderTest.convert(throwableIncident);

        // Assert
        assertThat(actual).isNotNull();
        assertThat(actual.getType()).isEqualTo("Throwable");
        assertThat(actual.getUsername().getIdentifier()).isEqualTo("Unknown");
        assertThat(actual.getAttributes()).hasSize(5);
        assertThat(actual.getAttributes()).containsOnlyKeys("incidentType", "Exception", "Message", "Trace", "Key1");
        assertThat(actual.getAttributes().get("incidentType")).isEqualTo("Throwable");
        assertThat(actual.getAttributes().get("Exception")).isEqualTo(NullPointerException.class);
        assertThat(actual.getAttributes().get("Message")).isEqualTo("Tech1");
        assertThat(actual.getAttributes().get("Trace").toString()).startsWith("Throwable occurred! Please take required actions!");
        assertThat(actual.getAttributes().get("Key1")).isEqualTo(object);
    }

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
        assertThat(actual.getAttributes()).containsOnlyKeys("incidentType", "Username", "Browser", "Country", "IP address", "What", "Where", "Exception");
        assertThat(actual.getAttributes().get("incidentType")).isEqualTo("Authentication Login");
        assertThat(actual.getAttributes().get("Username")).isEqualTo(username);
        assertThat(actual.getAttributes().get("Browser")).isEqualTo("[?]");
        assertThat(actual.getAttributes().get("Country")).isEqualTo("Unknown");
        assertThat(actual.getAttributes().get("IP address")).isEqualTo("8.8.8.8");
        assertThat(actual.getAttributes().get("What")).isEqualTo("[?], [?] on [?]");
        assertThat(actual.getAttributes().get("Where")).isEqualTo("Unknown, Unknown");
        assertThat(actual.getAttributes().get("Exception")).isEqualTo("exception1");
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
        assertThat(actual.getAttributes()).containsOnlyKeys("incidentType", "Username", "Browser", "Country", "IP address", "What", "Where");
        assertThat(actual.getAttributes().get("incidentType")).isEqualTo("Authentication Login");
        assertThat(actual.getAttributes().get("Username")).isEqualTo(username);
        assertThat(actual.getAttributes().get("Browser")).isEqualTo("[?]");
        assertThat(actual.getAttributes().get("Country")).isEqualTo("[?]");
        assertThat(actual.getAttributes().get("IP address")).isEqualTo("127.0.0.1");
        assertThat(actual.getAttributes().get("What")).isEqualTo("—");
        assertThat(actual.getAttributes().get("Where")).isEqualTo("Processing...Please wait!");
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
        assertThat(actual.getType()).isEqualTo("Authentication Login Failure");
        assertThat(actual.getUsername().getIdentifier()).isEqualTo("tech1");
        assertThat(actual.getAttributes()).hasSize(3);
        assertThat(actual.getAttributes()).containsOnlyKeys("incidentType", "Username", "Password");
        assertThat(actual.getAttributes().get("incidentType")).isEqualTo("Authentication Login Failure");
        assertThat(actual.getAttributes().get("Username")).isEqualTo(username);
        assertThat(actual.getAttributes().get("Password")).isEqualTo(password);
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
        assertThat(actual.getType()).isEqualTo("Authentication Login Failure");
        assertThat(actual.getUsername().getIdentifier()).isEqualTo("tech1");
        assertThat(actual.getAttributes()).hasSize(3);
        assertThat(actual.getAttributes()).containsOnlyKeys("incidentType", "Username", "Password");
        assertThat(actual.getAttributes().get("incidentType")).isEqualTo("Authentication Login Failure");
        assertThat(actual.getAttributes().get("Username")).isEqualTo(username);
        assertThat(actual.getAttributes().get("Password")).isEqualTo(Password.of("passw**********"));
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
        assertThat(actual.getType()).isEqualTo("Authentication Logout");
        assertThat(actual.getUsername().getIdentifier()).isEqualTo("tech1");
        assertThat(actual.getAttributes()).hasSize(2);
        assertThat(actual.getAttributes()).containsOnlyKeys("incidentType", "Username");
        assertThat(actual.getAttributes().get("incidentType")).isEqualTo("Authentication Logout");
        assertThat(actual.getAttributes().get("Username")).isEqualTo(username);
    }

    @Test
    public void convertAuthenticationLogoutFullIncidentTest() {
        // Arrange
        var username = Username.of("tech1");
        var incident = IncidentAuthenticationLogoutFull.of(
                username,
                UserRequestMetadata.processed(
                        GeoLocation.processed(new IPAddress("2.2.2.2"), "UK", "UK", "London"),
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
        assertThat(actual.getAttributes()).containsOnlyKeys("incidentType", "Username", "Browser", "Country", "IP address", "What", "Where");
        assertThat(actual.getAttributes().get("incidentType")).isEqualTo("Authentication Logout");
        assertThat(actual.getAttributes().get("Username")).isEqualTo(username);
        assertThat(actual.getAttributes().get("Browser")).isEqualTo("Mozilla");
        assertThat(actual.getAttributes().get("Country")).isEqualTo("UK");
        assertThat(actual.getAttributes().get("IP address")).isEqualTo("2.2.2.2");
        assertThat(actual.getAttributes().get("What")).isEqualTo("Mozilla, MacOS on Desktop");
        assertThat(actual.getAttributes().get("Where")).isEqualTo("UK, London");
    }

    @Test
    public void convertSessionRefreshedIncidentTest() {
        // Arrange
        var username = Username.of("tech1");
        var incident = IncidentSessionRefreshed.of(
                username,
                UserRequestMetadata.processed(
                        GeoLocation.processed(new IPAddress("2.2.2.2"), "UK", "UK", "London"),
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
        assertThat(actual.getAttributes()).containsOnlyKeys("incidentType", "Username", "Browser", "Country", "IP address", "What", "Where");
        assertThat(actual.getAttributes().get("incidentType")).isEqualTo("Session Refreshed");
        assertThat(actual.getAttributes().get("Username")).isEqualTo(username);
        assertThat(actual.getAttributes().get("Browser")).isEqualTo("Mozilla");
        assertThat(actual.getAttributes().get("Country")).isEqualTo("UK");
        assertThat(actual.getAttributes().get("IP address")).isEqualTo("2.2.2.2");
        assertThat(actual.getAttributes().get("What")).isEqualTo("Mozilla, MacOS on Desktop");
        assertThat(actual.getAttributes().get("Where")).isEqualTo("UK, London");
    }

    @Test
    public void convertSessionExpiredIncidentTest() {
        // Arrange
        var username = Username.of("tech1");
        var incident = IncidentSessionExpired.of(
                username,
                UserRequestMetadata.processed(
                        GeoLocation.processed(new IPAddress("2.2.2.2"), "UK", "UK", "London"),
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
        assertThat(actual.getAttributes()).containsOnlyKeys("incidentType", "Username", "Browser", "Country", "IP address", "What", "Where");
        assertThat(actual.getAttributes().get("incidentType")).isEqualTo("Session Expired");
        assertThat(actual.getAttributes().get("Username")).isEqualTo(username);
        assertThat(actual.getAttributes().get("Browser")).isEqualTo("Mozilla");
        assertThat(actual.getAttributes().get("Country")).isEqualTo("UK");
        assertThat(actual.getAttributes().get("IP address")).isEqualTo("2.2.2.2");
        assertThat(actual.getAttributes().get("What")).isEqualTo("Mozilla, MacOS on Desktop");
        assertThat(actual.getAttributes().get("Where")).isEqualTo("UK, London");
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
        assertThat(actual.getAttributes()).containsOnlyKeys("incidentType", "Username");
        assertThat(actual.getAttributes().get("incidentType")).isEqualTo("Register1");
        assertThat(actual.getAttributes().get("Username")).isEqualTo(username);
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
        assertThat(actual.getAttributes()).containsOnlyKeys("incidentType", "Username", "Exception", "Invitation Code");
        assertThat(actual.getAttributes().get("incidentType")).isEqualTo("Register1 Failure");
        assertThat(actual.getAttributes().get("Username")).isEqualTo(username);
        assertThat(actual.getAttributes().get("Exception")).isEqualTo(exception);
        assertThat(actual.getAttributes().get("Invitation Code")).isEqualTo(invitationCode);
    }
}
