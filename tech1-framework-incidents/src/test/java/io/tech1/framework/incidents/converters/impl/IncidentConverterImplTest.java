package io.tech1.framework.incidents.converters.impl;

import io.tech1.framework.domain.base.Username;
import io.tech1.framework.domain.geo.GeoLocation;
import io.tech1.framework.domain.http.requests.IPAddress;
import io.tech1.framework.domain.http.requests.UserAgentDetails;
import io.tech1.framework.domain.http.requests.UserRequestMetadata;
import io.tech1.framework.incidents.converters.IncidentConverter;
import io.tech1.framework.incidents.domain.authetication.AuthenticationLoginIncident;
import io.tech1.framework.incidents.domain.throwable.ThrowableIncident;
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
        var throwableIncident = ThrowableIncident.of(throwable);

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
        var throwableIncident = ThrowableIncident.of(throwable, method, params);

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
        assertThat(actual.getAttributes().get("Method")).isEqualTo("public void java.lang.Throwable.printStackTrace()");
        assertThat(actual.getAttributes().get("Params")).isEqualTo(object + ", param1, 1");
    }

    @Test
    public void convertThrowableIncident3Test() {
        // Arrange
        var object = new Object();
        var throwable = new NullPointerException("Tech1");
        var attributes = Map.of("Key1", object);
        var throwableIncident = ThrowableIncident.of(throwable, attributes);

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
        var incident = AuthenticationLoginIncident.of(
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
        var incident = AuthenticationLoginIncident.of(
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
}
