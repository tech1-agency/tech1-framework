package io.tech1.framework.domain.exceptions.cookies;

import org.junit.jupiter.api.Test;

import static io.tech1.framework.domain.utilities.random.RandomUtility.randomString;
import static org.assertj.core.api.Assertions.assertThat;

class CookieNotFoundExceptionTest {

    @Test
    void testException() {
        // Arrange
        var message = randomString();

        // Act
        var actual = new CookieNotFoundException(message);

        // Assert
        assertThat(actual.getMessage()).isEqualTo(message);
    }
}
