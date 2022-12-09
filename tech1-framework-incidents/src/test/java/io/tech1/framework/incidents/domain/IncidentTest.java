package io.tech1.framework.incidents.domain;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.Map;
import java.util.stream.Stream;

import static io.tech1.framework.domain.utilities.random.RandomUtility.randomString;
import static io.tech1.framework.incidents.tests.random.IncidentsRandomUtility.randomIncident;
import static org.assertj.core.api.Assertions.assertThat;

public class IncidentTest {

    private static Stream<Arguments> getTypeTest() {
        return Stream.of(
                Arguments.of(Map.of(randomString(), randomString()), "Unknown"),
                Arguments.of(Map.of("incidentType", "IncidentType123"), "IncidentType123")
        );
    }

    private static Stream<Arguments> getUsernameTest() {
        return Stream.of(
                Arguments.of(Map.of(randomString(), randomString()), "Unknown"),
                Arguments.of(Map.of("username", "Username123"), "Username123")
        );
    }

    @Test
    public void copyOfTest() {
        // Arrange
        var incident = randomIncident();

        // Act
        var copy = Incident.copyOf(incident);

        // Assert
        assertThat(copy.getAttributes()).isEqualTo(incident.getAttributes());
        assertThat(copy.getType()).isEqualTo(incident.getType());
        assertThat(copy.getUsername()).isEqualTo(incident.getUsername());
    }


    @ParameterizedTest
    @MethodSource("getTypeTest")
    public void getTypeTest(Map<String, Object> attributes, String expected) {
        // Arrange
        var incident = new Incident();
        incident.addAll(attributes);

        // Act
        var actual = incident.getType();

        // Assert
        assertThat(actual).isEqualTo(expected);
    }

    @ParameterizedTest
    @MethodSource("getUsernameTest")
    public void getUsernameTest(Map<String, Object> attributes, String expected) {
        // Arrange
        var incident = new Incident();
        incident.addAll(attributes);

        // Act
        var actual = incident.getUsername();

        // Assert
        assertThat(actual.getIdentifier()).isEqualTo(expected);
    }
}
