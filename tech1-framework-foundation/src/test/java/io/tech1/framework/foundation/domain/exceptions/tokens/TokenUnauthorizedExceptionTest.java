package io.tech1.framework.foundation.domain.exceptions.tokens;

import org.junit.jupiter.api.Test;

import static io.tech1.framework.foundation.utilities.random.RandomUtility.randomString;
import static org.assertj.core.api.Assertions.assertThat;

class TokenUnauthorizedExceptionTest {

    @Test
    void testException() {
        // Arrange
        var message = randomString();

        // Act
        var actual = new TokenUnauthorizedException(message);

        // Assert
        assertThat(actual.getMessage()).isEqualTo(message);
    }
}
