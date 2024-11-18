package tech1.framework.foundation.domain.exceptions.tokens;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class RefreshTokenInvalidExceptionTest {

    @Test
    void testException() {
        // Act
        var actual = new RefreshTokenInvalidException();

        // Assert
        assertThat(actual.getMessage()).isEqualTo("JWT refresh token is invalid");
    }
}
