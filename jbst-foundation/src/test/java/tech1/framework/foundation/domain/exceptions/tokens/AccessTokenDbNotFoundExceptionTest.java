package tech1.framework.foundation.domain.exceptions.tokens;

import tech1.framework.foundation.domain.base.Username;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class AccessTokenDbNotFoundExceptionTest {

    @Test
    void testException() {
        // Arrange
        var username = Username.random();

        // Act
        var actual = new AccessTokenDbNotFoundException(username);

        // Assert
        assertThat(actual.getMessage()).isEqualTo("JWT access token is not present in database. Username: " + username);
    }
}
