package io.tech1.framework.domain.exceptions.random;

import io.tech1.framework.domain.tests.enums.EnumUnderTests;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class IllegalEnumExceptionTest {

    @Test
    public void testException() {
        // Arrange
        var clazz = EnumUnderTests.class;

        // Act
        var actual = new IllegalEnumException(clazz);

        // Assert
        assertThat(actual.getMessage()).isEqualTo("Please check enum: class io.tech1.framework.domain.tests.enums.EnumUnderTests");
    }
}
