package tech1.framework.foundation.domain.exceptions.tokens;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class CsrfTokenNotFoundExceptionTest {

    @Test
    void testException() {
        // Act
        var actual = new CsrfTokenNotFoundException();

        // Assert
        assertThat(actual.getMessage()).isEqualTo("Csrf token not found");
    }
}
