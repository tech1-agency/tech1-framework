package jbst.foundation.domain.exceptions.base;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class JbstUnreachableExceptionTest {

    @Test
    void testException() {
        // Act
        var actual = new JbstUnreachableCodeException();

        // Assert
        assertThat(actual.getMessage()).isEqualTo("Unreachable code. Please contact development team");
    }
}
