package io.tech1.framework.domain.exceptions.tokens;

import io.tech1.framework.domain.exceptions.tokens.RefreshTokenNotFoundException;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class RefreshTokenNotFoundExceptionTest {

    @Test
    void testException() {
        // Act
        var actual = new RefreshTokenNotFoundException();

        // Assert
        assertThat(actual.getMessage()).isEqualTo("JWT refresh token not found");
    }
}
