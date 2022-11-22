package io.tech1.framework.domain.utilities.exceptions;

import io.tech1.framework.domain.exceptions.ThrowableTrace;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.stream.Stream;

import static io.tech1.framework.domain.utilities.exceptions.ThrowableUtility.getTrace;
import static org.assertj.core.api.Assertions.assertThat;

public class ThrowableUtilityTest {

    private static Stream<Arguments> getTraceTest() {
        return Stream.of(
                Arguments.of(4000, 4051),
                Arguments.of(3000, 3051),
                Arguments.of(2000, 2051),
                Arguments.of(201, 252),
                Arguments.of(200, 251),
                Arguments.of(199, 251),
                Arguments.of(198, 251),
                Arguments.of(100, 251),
                Arguments.of(10, 251)
        );
    }

    @ParameterizedTest
    @MethodSource("getTraceTest")
    public void getTraceTest(int length, int expected) {
        // Arrange
        var npe = new NullPointerException("Tech1");

        // Act
        ThrowableTrace actual;
        if (length == 3000) {
            actual = getTrace(npe);
        } else {
            actual = getTrace(npe, length);
        }

        // Arrange
        assertThat(actual).isNotNull();
        assertThat(actual.getValue()).isNotNull();
        assertThat(actual.getValue().length()).isEqualTo(expected);
        assertThat(actual.getValue()).startsWith("Throwable occurred! Please take required actions!");
        assertThat(actual.getValue()).contains("java.lang.NullPointerException: Tech1");
        assertThat(actual.toString()).contains("java.lang.NullPointerException: Tech1");
        System.out.println(actual.getValue());
    }
}
