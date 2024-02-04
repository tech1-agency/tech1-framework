package io.tech1.framework.domain.exceptions.cookie;

import io.tech1.framework.domain.exceptions.tokens.CookieUnauthorizedException;
import org.junit.jupiter.api.Test;

import static io.tech1.framework.domain.utilities.random.RandomUtility.randomString;
import static org.assertj.core.api.Assertions.assertThat;

class CookieUnauthorizedExceptionTest {

    @Test
    void testException() {
        // Arrange
        var message = randomString();

        // Act
        var actual = new CookieUnauthorizedException(message);

        // Assert
        assertThat(actual.getMessage()).isEqualTo(message);
    }
}
