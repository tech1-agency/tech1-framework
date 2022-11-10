package io.tech1.framework.domain.utilities.strings;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.stream.Stream;

import static io.tech1.framework.domain.utilities.random.RandomUtility.randomString;
import static io.tech1.framework.domain.utilities.strings.StringUtility.isNullOrBlank;
import static io.tech1.framework.domain.utilities.strings.StringUtility.isNullOrEmpty;
import static org.assertj.core.api.Assertions.assertThat;

public class StringUtilityTest {

    private static Stream<Arguments> isNullOrEmptyTest() {
        return Stream.of(
                Arguments.of(null, true),
                Arguments.of("null", false),
                Arguments.of("", true),
                Arguments.of(" ", false),
                Arguments.of("   ", false),
                Arguments.of("        ", false),
                Arguments.of("     t1   ", false),
                Arguments.of(randomString(), false)
        );
    }

    private static Stream<Arguments> isNullOrBlankTest() {
        return Stream.of(
                Arguments.of(null, true),
                Arguments.of("null", false),
                Arguments.of("", true),
                Arguments.of(" ", true),
                Arguments.of("   ", true),
                Arguments.of("        ", true),
                Arguments.of("     t1   ", false),
                Arguments.of(randomString(), false)
        );
    }

    @ParameterizedTest
    @MethodSource("isNullOrEmptyTest")
    public void isNullOrEmptyTest(String value, boolean expected) {
        // Act
        var actual = isNullOrEmpty(value);

        // Assert
        assertThat(actual).isEqualTo(expected);
    }

    @ParameterizedTest
    @MethodSource("isNullOrBlankTest")
    public void isNullOrBlankTest(String value, boolean expected) {
        // Act
        var actual = isNullOrBlank(value);

        // Assert
        assertThat(actual).isEqualTo(expected);
    }
}
