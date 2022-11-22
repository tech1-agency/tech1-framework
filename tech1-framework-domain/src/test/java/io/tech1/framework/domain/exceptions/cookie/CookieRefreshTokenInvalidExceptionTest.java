package io.tech1.framework.domain.exceptions.cookie;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class CookieRefreshTokenInvalidExceptionTest {

    @Test
    public void testException() {
        // Act
        var actual = new CookieRefreshTokenInvalidException();

        // Assert
        assertThat(actual.getMessage()).isEqualTo("JWT refresh token is invalid");
    }
}
