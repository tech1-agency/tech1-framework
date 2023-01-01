package io.tech1.framework.incidents.converters.impl;

import io.tech1.framework.incidents.converters.IncidentConverter;
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
public class IncidentConverterImplTest {

    @Configuration
    static class ContextConfiguration {
        @Bean
        IncidentConverter incidentConverter() {
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
        assertThat(actual.getAttributes()).containsOnlyKeys("incidentType", "exception", "message", "trace");
        assertThat(actual.getAttributes().get("incidentType")).isEqualTo("Throwable");
        assertThat(actual.getAttributes().get("exception")).isEqualTo(NullPointerException.class);
        assertThat(actual.getAttributes().get("message")).isEqualTo("Tech1");
        assertThat(actual.getAttributes().get("trace").toString()).startsWith("Throwable occurred! Please take required actions!");
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
        assertThat(actual.getAttributes()).containsOnlyKeys("incidentType", "exception", "message", "trace", "method", "params");
        assertThat(actual.getAttributes().get("incidentType")).isEqualTo("Throwable");
        assertThat(actual.getAttributes().get("exception")).isEqualTo(NullPointerException.class);
        assertThat(actual.getAttributes().get("message")).isEqualTo("Tech1");
        assertThat(actual.getAttributes().get("trace").toString()).startsWith("Throwable occurred! Please take required actions!");
        assertThat(actual.getAttributes().get("method").toString()).contains("protected void java.lang.Object.finalize() throws java.lang.Throwable");
        assertThat(actual.getAttributes().get("params")).isEqualTo(object + ", param1, 1");
    }

    @Test
    public void convertThrowableIncident3Test() {
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
        assertThat(actual.getUsername().getIdentifier()).isEqualTo("Unknown");
        assertThat(actual.getAttributes()).hasSize(5);
        assertThat(actual.getAttributes()).containsOnlyKeys("incidentType", "exception", "message", "trace", "key1");
        assertThat(actual.getAttributes().get("incidentType")).isEqualTo("Throwable");
        assertThat(actual.getAttributes().get("exception")).isEqualTo(NullPointerException.class);
        assertThat(actual.getAttributes().get("message")).isEqualTo("Tech1");
        assertThat(actual.getAttributes().get("trace").toString()).startsWith("Throwable occurred! Please take required actions!");
        assertThat(actual.getAttributes().get("key1")).isEqualTo(object);
    }
}
