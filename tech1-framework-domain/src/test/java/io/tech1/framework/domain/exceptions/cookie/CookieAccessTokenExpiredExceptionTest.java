package io.tech1.framework.domain.exceptions.cookie;

import org.junit.jupiter.api.Test;

import static io.tech1.framework.domain.utilities.random.RandomUtility.randomUsername;
import static org.assertj.core.api.Assertions.assertThat;

class CookieAccessTokenExpiredExceptionTest {

    @Test
    void testException() {
        // Arrange
        var username = randomUsername();

        // Act
        var actual = new CookieAccessTokenExpiredException(username);

        // Assert
        assertThat(actual.getMessage()).isEqualTo("JWT access token is expired. Username: " + username);
    }
}
