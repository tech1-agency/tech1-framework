package io.tech1.framework.domain.exceptions.authentication;

import org.junit.jupiter.api.Test;

import static io.tech1.framework.domain.utilities.random.RandomUtility.randomString;
import static org.assertj.core.api.Assertions.assertThat;

public class RegistrationExceptionTest {

    @Test
    public void testException() {
        // Arrange
        var message = randomString();

        // Act
        var actual = new RegistrationException(message);

        // Assert
        assertThat(actual.getMessage()).isEqualTo(message);
    }
}
