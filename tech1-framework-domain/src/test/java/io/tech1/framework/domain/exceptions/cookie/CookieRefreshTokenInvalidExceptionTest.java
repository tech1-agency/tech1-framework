package io.tech1.framework.domain.exceptions.cookie;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class CookieRefreshTokenInvalidExceptionTest {

    @Test
    void testException() {
        // Act
        var actual = new CookieRefreshTokenInvalidException();

        // Assert
        assertThat(actual.getMessage()).isEqualTo("JWT refresh token is invalid");
    }
}
