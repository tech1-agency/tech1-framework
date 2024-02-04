package io.tech1.framework.domain.exceptions.cookie;

import io.tech1.framework.domain.base.Username;
import io.tech1.framework.domain.exceptions.tokens.CookieAccessTokenExpiredException;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class CookieAccessTokenExpiredExceptionTest {

    @Test
    void testException() {
        // Arrange
        var username = Username.random();

        // Act
        var actual = new CookieAccessTokenExpiredException(username);

        // Assert
        assertThat(actual.getMessage()).isEqualTo("JWT access token is expired. Username: " + username);
    }
}
