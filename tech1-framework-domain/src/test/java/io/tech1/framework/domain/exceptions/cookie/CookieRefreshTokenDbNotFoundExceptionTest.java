package io.tech1.framework.domain.exceptions.cookie;

import org.junit.jupiter.api.Test;

import static io.tech1.framework.domain.utilities.random.RandomUtility.randomUsername;
import static org.assertj.core.api.Assertions.assertThat;

class CookieRefreshTokenDbNotFoundExceptionTest {

    @Test
    void testException() {
        // Arrange
        var username = randomUsername();

        // Act
        var actual = new CookieRefreshTokenDbNotFoundException(username);

        // Assert
        assertThat(actual.getMessage()).isEqualTo("JWT refresh token is not present in database. Username: " + username);
    }
}
