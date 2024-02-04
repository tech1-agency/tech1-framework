package io.tech1.framework.domain.exceptions.cookie;

import io.tech1.framework.domain.exceptions.tokens.CookieAccessTokenInvalidException;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class CookieAccessTokenInvalidExceptionTest {

    @Test
    void testException() {
        // Act
        var actual = new CookieAccessTokenInvalidException();

        // Assert
        assertThat(actual.getMessage()).isEqualTo("JWT access token is invalid");
    }
}
