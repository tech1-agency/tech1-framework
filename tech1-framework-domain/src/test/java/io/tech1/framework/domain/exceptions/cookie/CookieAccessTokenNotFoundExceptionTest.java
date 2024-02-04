package io.tech1.framework.domain.exceptions.cookie;

import io.tech1.framework.domain.exceptions.tokens.CookieAccessTokenNotFoundException;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class CookieAccessTokenNotFoundExceptionTest {

    @Test
    void testException() {
        // Act
        var actual = new CookieAccessTokenNotFoundException();

        // Assert
        assertThat(actual.getMessage()).isEqualTo("JWT access token not found");
    }
}
