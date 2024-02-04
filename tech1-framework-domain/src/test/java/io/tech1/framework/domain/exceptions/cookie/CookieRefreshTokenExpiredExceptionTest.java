package io.tech1.framework.domain.exceptions.cookie;

import io.tech1.framework.domain.base.Username;
import io.tech1.framework.domain.exceptions.tokens.CookieRefreshTokenExpiredException;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class CookieRefreshTokenExpiredExceptionTest {

    @Test
    void testException() {
        // Arrange
        var username = Username.random();

        // Act
        var actual = new CookieRefreshTokenExpiredException(username);

        // Assert
        assertThat(actual.getMessage()).isEqualTo("JWT refresh token is expired. Username: " + username);
    }
}
