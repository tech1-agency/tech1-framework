package io.tech1.framework.domain.exceptions.cookie;

import io.tech1.framework.domain.exceptions.tokens.CookieRefreshTokenNotFoundException;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class CookieRefreshTokenNotFoundExceptionTest {

    @Test
    void testException() {
        // Act
        var actual = new CookieRefreshTokenNotFoundException();

        // Assert
        assertThat(actual.getMessage()).isEqualTo("JWT refresh token not found");
    }
}
