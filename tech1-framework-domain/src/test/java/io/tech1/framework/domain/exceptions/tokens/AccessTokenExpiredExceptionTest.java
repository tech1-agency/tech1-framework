package io.tech1.framework.domain.exceptions.tokens;

import io.tech1.framework.domain.base.Username;
import io.tech1.framework.domain.exceptions.tokens.AccessTokenExpiredException;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class AccessTokenExpiredExceptionTest {

    @Test
    void testException() {
        // Arrange
        var username = Username.random();

        // Act
        var actual = new AccessTokenExpiredException(username);

        // Assert
        assertThat(actual.getMessage()).isEqualTo("JWT access token is expired. Username: " + username);
    }
}
