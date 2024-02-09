package io.tech1.framework.domain.utilities.enums;

import io.tech1.framework.domain.tests.enums.EnumValue1;
import io.tech1.framework.domain.tests.enums.EnumValue2;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.stream.Stream;

import static io.tech1.framework.domain.tests.enums.EnumValue1.FRAMEWORK;
import static io.tech1.framework.domain.tests.enums.EnumValue1.TECH1;
import static io.tech1.framework.domain.tests.enums.EnumValue2.UNKNOWN;
import static io.tech1.framework.domain.utilities.enums.EnumCreatorUtility.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.catchThrowable;

class EnumCreatorUtilityTest {

    private static Stream<Arguments> findEnumByValueIgnoreCaseOrThrowArgs() {
        return Stream.of(
                Arguments.of("Tech1", false, TECH1, null),
                Arguments.of("tech1", false, TECH1, null),
                Arguments.of("TECh1", false, TECH1, null),
                Arguments.of("Framework", false, FRAMEWORK, null),
                Arguments.of("framework", false, FRAMEWORK, null),
                Arguments.of("fraMEwork", false, FRAMEWORK, null),
                Arguments.of("Tech2", true, null, "Required values: `[Framework, Tech1]`. Missing values: `[Tech2]`"),
                Arguments.of("Server", true, null, "Required values: `[Framework, Tech1]`. Missing values: `[Server]`"),
                Arguments.of(null, true, null, "Required values: `[Framework, Tech1]`. Missing values: `[null]`")
        );
    }

    private static Stream<Arguments> findEnumByNameOrThrowArgs() {
        return Stream.of(
                Arguments.of("TECH1", false, TECH1, null),
                Arguments.of("tech1", true, null, "Required values: `[Framework, Tech1]`. Missing values: `[tech1]`"),
                Arguments.of("TECh1", true, null, "Required values: `[Framework, Tech1]`. Missing values: `[TECh1]`"),
                Arguments.of("FRAMEWORK", false, FRAMEWORK, null),
                Arguments.of("framework", true, null, "Required values: `[Framework, Tech1]`. Missing values: `[framework]`"),
                Arguments.of("fraMEwork", true, null, "Required values: `[Framework, Tech1]`. Missing values: `[fraMEwork]`"),
                Arguments.of("Tech2", true, null, "Required values: `[Framework, Tech1]`. Missing values: `[Tech2]`"),
                Arguments.of("Server", true, null, "Required values: `[Framework, Tech1]`. Missing values: `[Server]`"),
                Arguments.of(null, true, null, "Required values: `[Framework, Tech1]`. Missing values: `[null]`")
        );
    }

    @ParameterizedTest
    @MethodSource("findEnumByValueIgnoreCaseOrThrowArgs")
    void findEnumByValueIgnoreCaseOrThrowTest(String name, boolean exception, EnumValue1 expected, String expectedMessage) {
        // Act
        var throwable = catchThrowable(() -> {
            // Act
            var actual = findEnumByValueIgnoreCaseOrThrow(EnumValue1.class, name);

            // Assert
            assertThat(actual).isEqualTo(expected);
        });

        // Assert
        if (exception) {
            assertThat(throwable)
                    .isInstanceOf(IllegalArgumentException.class)
                    .hasMessageStartingWith("Attribute `EnumValue1` is invalid")
                    .hasMessageEndingWith(expectedMessage);
        }
    }

    @ParameterizedTest
    @MethodSource("findEnumByNameOrThrowArgs")
    void findEnumByNameOrThrowTest(String name, boolean exception, EnumValue1 expected, String expectedMessage) {
        // Act
        var throwable = catchThrowable(() -> {
            // Act
            var actual = findEnumByNameOrThrow(EnumValue1.class, name);

            // Assert
            assertThat(actual).isEqualTo(expected);
        });

        // Assert
        if (exception) {
            assertThat(throwable)
                    .isInstanceOf(IllegalArgumentException.class)
                    .hasMessageStartingWith("Attribute `EnumValue1` is invalid")
                    .hasMessageEndingWith(expectedMessage);
        }
    }

    @Test
    void findUnknownValueOk() {
        // Act
        var actual = findUnknownValue(EnumValue2.class);

        // Assert
        assertThat(actual).isEqualTo(UNKNOWN);
    }

    @Test
    void findUnknownValueFailure() {
        // Act
        var throwable = catchThrowable(() -> findUnknownValue(EnumValue1.class));

        // Assert
        assertThat(throwable)
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageStartingWith("EnumValue1 does not have UNKNOWN enum value");
    }
}
