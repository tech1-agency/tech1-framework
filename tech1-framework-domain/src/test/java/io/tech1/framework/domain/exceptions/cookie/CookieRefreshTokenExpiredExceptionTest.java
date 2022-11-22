package io.tech1.framework.domain.exceptions.cookie;

import org.junit.jupiter.api.Test;

import static io.tech1.framework.domain.utilities.random.RandomUtility.randomUsername;
import static org.assertj.core.api.Assertions.assertThat;

public class CookieRefreshTokenExpiredExceptionTest {

    @Test
    public void testException() {
        // Arrange
        var username = randomUsername();

        // Act
        var actual = new CookieRefreshTokenExpiredException(username);

        // Assert
        assertThat(actual.getMessage()).isEqualTo("JWT refresh token is expired. Username: " + username);
    }
}
