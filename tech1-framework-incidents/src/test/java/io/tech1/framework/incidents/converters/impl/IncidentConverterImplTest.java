package io.tech1.framework.incidents.converters.impl;

import io.tech1.framework.domain.base.Password;
import io.tech1.framework.domain.base.Username;
import io.tech1.framework.domain.base.UsernamePasswordCredentials;
import io.tech1.framework.domain.http.requests.UserRequestMetadata;
import io.tech1.framework.incidents.converters.IncidentConverter;
import io.tech1.framework.incidents.domain.authetication.IncidentAuthenticationLoginFailureUsernameMaskedPassword;
import io.tech1.framework.incidents.domain.authetication.IncidentAuthenticationLoginFailureUsernamePassword;
import io.tech1.framework.incidents.domain.authetication.IncidentAuthenticationLogoutMin;
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
import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith({ SpringExtension.class })
@ContextConfiguration(loader= AnnotationConfigContextLoader.class)
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
class IncidentConverterImplTest {

    @Configuration
    static class ContextConfiguration {
        @Bean
        IncidentConverter incidentConverter() {
            return new IncidentConverterImpl();
        }
    }

    private final IncidentConverter componentUnderTest;

    @Test
    void convertThrowableIncident1Test() {
        // Arrange
        var throwable = new NullPointerException("Tech1");
        var throwableIncident = IncidentThrowable.of(throwable);

        // Act
        var actual = this.componentUnderTest.convert(throwableIncident);

        // Assert
        assertThat(actual).isNotNull();
        assertThat(actual.getType()).isEqualTo("Throwable");
        assertThat(actual.getUsername().value()).isEqualTo("Unknown");
        assertThat(actual.getAttributes()).hasSize(4);
        assertThat(actual.getAttributes()).containsOnlyKeys("incidentType", "exception", "message", "trace");
        assertThat(actual.getAttributes()).containsEntry("incidentType", "Throwable");
        assertThat(actual.getAttributes()).containsEntry("exception", NullPointerException.class);
        assertThat(actual.getAttributes()).containsEntry("message", "Tech1");
        assertThat(actual.getAttributes().get("trace").toString()).startsWith("java.lang.NullPointerException: Tech1");
        assertThat(actual.getAttributes().get("trace").toString()).contains("at io.tech1.framework.incidents.converters.impl.IncidentConverterImplTest.convertThrowableIncident1Test");
    }

    @Test
    void convertThrowableIncident2Test() {
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
        assertThat(actual.getUsername().value()).isEqualTo("Unknown");
        assertThat(actual.getAttributes()).hasSize(6);
        assertThat(actual.getAttributes()).containsOnlyKeys("incidentType", "exception", "message", "trace", "method", "params");
        assertThat(actual.getAttributes()).containsEntry("incidentType", "Throwable");
        assertThat(actual.getAttributes()).containsEntry("exception", NullPointerException.class);
        assertThat(actual.getAttributes()).containsEntry("message", "Tech1");
        assertThat(actual.getAttributes()).containsEntry("params", object + ", param1, 1");
        assertThat(actual.getAttributes().get("trace").toString()).startsWith("java.lang.NullPointerException: Tech1");
        assertThat(actual.getAttributes().get("trace").toString()).contains("at io.tech1.framework.incidents.converters.impl.IncidentConverterImplTest.convertThrowableIncident2Test");
        assertThat(actual.getAttributes().get("method").toString()).contains("protected void java.lang.Object.finalize() throws java.lang.Throwable");
    }

    @Test
    void convertThrowableIncident3Test() {
        // Arrange
        var object = new Object();
        var throwable = new NullPointerException("Tech1");
        var attributes = Map.of("key1", object);
        var throwableIncident = IncidentThrowable.of(throwable, attributes);

        // Act
        var actual = this.componentUnderTest.convert(throwableIncident);

        // Assert
        assertThat(actual).isNotNull();
        assertThat(actual.getType()).isEqualTo("Throwable");
        assertThat(actual.getUsername().value()).isEqualTo("Unknown");
        assertThat(actual.getAttributes()).hasSize(5);
        assertThat(actual.getAttributes()).containsOnlyKeys("incidentType", "exception", "message", "trace", "key1");
        assertThat(actual.getAttributes()).containsEntry("incidentType", "Throwable");
        assertThat(actual.getAttributes()).containsEntry("exception", NullPointerException.class);
        assertThat(actual.getAttributes()).containsEntry("message", "Tech1");
        assertThat(actual.getAttributes()).containsEntry("key1", object);
        assertThat(actual.getAttributes().get("trace").toString()).startsWith("java.lang.NullPointerException: Tech1");
        assertThat(actual.getAttributes().get("trace").toString()).contains("at io.tech1.framework.incidents.converters.impl.IncidentConverterImplTest.convertThrowableIncident3Test");
    }

    // TODO [YYL-clean]
    @Test
    void convertAuthenticationLoginFailureUsernamePasswordIncidentTest() {
        // Arrange
        var incident = new IncidentAuthenticationLoginFailureUsernamePassword(
                UsernamePasswordCredentials.testsHardcoded(),
                UserRequestMetadata.valid()
        );

        // Act
        var actual = this.componentUnderTest.convert(incident);

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

    // TODO [YYL-clean]
    @Test
    void convertAuthenticationLoginFailureUsernameMaskedPasswordIncidentTest() {
        // Arrange
        var incident = new IncidentAuthenticationLoginFailureUsernameMaskedPassword(
                UsernamePasswordCredentials.mask5(
                        Username.testsHardcoded(),
                        Password.testsHardcoded()
                ),
                UserRequestMetadata.valid()
        );

        // Act
        var actual = this.componentUnderTest.convert(incident);

        // Assert
        assertThat(actual).isNotNull();
        assertThat(actual.getType()).isEqualTo("Authentication Login Failure Username/Masked Password");
        assertThat(actual.getUsername().value()).isEqualTo("tech1");
        assertThat(actual.getAttributes()).hasSize(3);
        assertThat(actual.getAttributes()).containsOnlyKeys("incidentType", "username", "password");
        assertThat(actual.getAttributes()).containsEntry("incidentType", "Authentication Login Failure Username/Masked Password");
        assertThat(actual.getAttributes()).containsEntry("username", Username.testsHardcoded());
        assertThat(actual.getAttributes()).containsEntry("password", new Password("Passw***********"));
    }

    @Test
    void convertAuthenticationLogoutMinIncidentTest() {
        // Arrange
        var username = Username.of("tech1");
        var incident = new IncidentAuthenticationLogoutMin(
                username
        );

        // Act
        var actual = this.componentUnderTest.convert(incident);

        // Assert
        assertThat(actual).isNotNull();
        assertThat(actual.getType()).isEqualTo("Authentication Logout Min");
        assertThat(actual.getUsername().value()).isEqualTo("tech1");
        assertThat(actual.getAttributes()).hasSize(2);
        assertThat(actual.getAttributes()).containsOnlyKeys("incidentType", "username");
        assertThat(actual.getAttributes()).containsEntry("incidentType", "Authentication Logout Min");
        assertThat(actual.getAttributes()).containsEntry("username", username);
    }
}
