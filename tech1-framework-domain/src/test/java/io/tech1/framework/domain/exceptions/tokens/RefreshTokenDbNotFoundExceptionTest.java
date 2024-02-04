package io.tech1.framework.domain.exceptions.tokens;

import io.tech1.framework.domain.base.Username;
import io.tech1.framework.domain.exceptions.tokens.RefreshTokenDbNotFoundException;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class RefreshTokenDbNotFoundExceptionTest {

    @Test
    void testException() {
        // Arrange
        var username = Username.random();

        // Act
        var actual = new RefreshTokenDbNotFoundException(username);

        // Assert
        assertThat(actual.getMessage()).isEqualTo("JWT refresh token is not present in database. Username: " + username);
    }
}
