package tech1.framework.foundation.domain.exceptions.tokens;

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
