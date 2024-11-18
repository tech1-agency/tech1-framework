package jbst.foundation.domain.exceptions.geo;

import org.junit.jupiter.api.Test;

import static jbst.foundation.utilities.random.RandomUtility.randomString;
import static org.assertj.core.api.Assertions.assertThat;

class GeoLocationNotFoundExceptionTest {

    @Test
    void testException() {
        // Arrange
        var message = randomString();

        // Act
        var actual = new GeoLocationNotFoundException(message);

        // Assert
        assertThat(actual.getMessage()).isEqualTo("Geo location not found: " + message);
    }
}
