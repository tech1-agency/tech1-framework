package io.tech1.framework.domain.utilities.enums;

import io.tech1.framework.domain.tests.enums.EnumValue1;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.stream.Stream;

import static io.tech1.framework.domain.tests.enums.EnumValue1.FRAMEWORK;
import static io.tech1.framework.domain.tests.enums.EnumValue1.TECH1;
import static io.tech1.framework.domain.utilities.enums.EnumCreatorUtility.findEnumIgnoreCaseOrThrow;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.catchThrowable;

class EnumCreatorUtilityTest {

    private static Stream<Arguments> ignoreCaseOkArgs() {
        return Stream.of(
                Arguments.of("Tech1", TECH1),
                Arguments.of("tech1", TECH1),
                Arguments.of("TECh1", TECH1),
                Arguments.of("Framework", FRAMEWORK),
                Arguments.of("framework", FRAMEWORK),
                Arguments.of("fraMEwork", FRAMEWORK)
        );
    }

    private static Stream<Arguments> ignoreCaseFailureArgs() {
        return Stream.of(
                Arguments.of("Tech2", "Attribute `io.tech1.framework.domain.tests.enums.EnumValue1` is invalid. Required values: `[Framework, Tech1]`. Missing values: `[Tech2]`"),
                Arguments.of("Server", "Attribute `io.tech1.framework.domain.tests.enums.EnumValue1` is invalid. Required values: `[Framework, Tech1]`. Missing values: `[Server]`"),
                Arguments.of(null, "Attribute `io.tech1.framework.domain.tests.enums.EnumValue1` is invalid. Required values: `[Framework, Tech1]`. Missing values: `[null]`")
        );
    }

    @ParameterizedTest
    @MethodSource("ignoreCaseOkArgs")
    void ignoreCaseOkArgs(String name, EnumValue1 expected) {
        // Act
        var actual = findEnumIgnoreCaseOrThrow(EnumValue1.class, name);

        // Assert
        assertThat(actual).isEqualTo(expected);
    }

    @ParameterizedTest
    @MethodSource("ignoreCaseFailureArgs")
    void ignoreCaseFailureArgs(String name, String expectedMessage) {
        // Act
        var throwable = catchThrowable(() -> findEnumIgnoreCaseOrThrow(EnumValue1.class, name));

        // Assert
        assertThat(throwable)
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining(expectedMessage);
    }
}
