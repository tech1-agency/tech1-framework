package io.tech1.framework.domain.exceptions.tokens;

import io.tech1.framework.domain.exceptions.tokens.AccessTokenNotFoundException;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class AccessTokenNotFoundExceptionTest {

    @Test
    void testException() {
        // Act
        var actual = new AccessTokenNotFoundException();

        // Assert
        assertThat(actual.getMessage()).isEqualTo("JWT access token not found");
    }
}
