package io.tech1.framework.domain.exceptions.geo;

import org.junit.jupiter.api.Test;

import static io.tech1.framework.domain.utilities.random.RandomUtility.randomString;
import static org.assertj.core.api.Assertions.assertThat;

public class GeoLocationNotFoundExceptionTest {

    @Test
    public void testException() {
        // Arrange
        var message = randomString();

        // Act
        var actual = new GeoLocationNotFoundException(message);

        // Assert
        assertThat(actual.getMessage()).isEqualTo("Geo location not found: " + message);
    }
}
