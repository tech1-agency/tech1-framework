package io.tech1.framework.domain.exceptions.cookie;

import io.tech1.framework.domain.base.Username;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class CookieAccessTokenDbNotFoundExceptionTest {

    @Test
    void testException() {
        // Arrange
        var username = Username.random();

        // Act
        var actual = new CookieAccessTokenDbNotFoundException(username);

        // Assert
        assertThat(actual.getMessage()).isEqualTo("JWT access token is not present in database. Username: " + username);
    }
}
