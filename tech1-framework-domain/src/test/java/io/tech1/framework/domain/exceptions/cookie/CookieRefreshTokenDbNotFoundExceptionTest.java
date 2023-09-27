package io.tech1.framework.domain.exceptions.cookie;

import io.tech1.framework.domain.base.Username;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class CookieRefreshTokenDbNotFoundExceptionTest {

    @Test
    void testException() {
        // Arrange
        var username = Username.random();

        // Act
        var actual = new CookieRefreshTokenDbNotFoundException(username);

        // Assert
        assertThat(actual.getMessage()).isEqualTo("JWT refresh token is not present in database. Username: " + username);
    }
}
