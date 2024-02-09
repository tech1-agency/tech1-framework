package io.tech1.framework.domain.utilities.enums;

import io.tech1.framework.domain.tests.enums.EnumValue1;
import io.tech1.framework.domain.tests.enums.EnumValue2;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.stream.Stream;

import static io.tech1.framework.domain.tests.enums.EnumValue2.UNKNOWN;
import static io.tech1.framework.domain.utilities.enums.EnumCreatorUtility.*;
import static io.tech1.framework.domain.utilities.random.RandomUtility.randomString;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.catchThrowable;

class EnumCreatorUtilityTest {

    private static Stream<Arguments> findEnumByValueIgnoreCaseOrThrowArgs() {
        return Stream.of(
                Arguments.of("Tech1", false, EnumValue1.TECH1, null),
                Arguments.of("tech1", false, EnumValue1.TECH1, null),
                Arguments.of("TECh1", false, EnumValue1.TECH1, null),
                Arguments.of("Framework", false, EnumValue1.FRAMEWORK, null),
                Arguments.of("framework", false, EnumValue1.FRAMEWORK, null),
                Arguments.of("fraMEwork", false, EnumValue1.FRAMEWORK, null),
                Arguments.of("Tech2", true, null, "Required values: `[Framework, Tech1]`. Missing values: `[Tech2]`"),
                Arguments.of("Server", true, null, "Required values: `[Framework, Tech1]`. Missing values: `[Server]`"),
                Arguments.of(null, true, null, "Required values: `[Framework, Tech1]`. Missing values: `[null]`")
        );
    }

    private static Stream<Arguments> findEnumByNameOrThrowArgs() {
        return Stream.of(
                Arguments.of("TECH1", false, EnumValue1.TECH1, null),
                Arguments.of("tech1", true, null, "Required values: `[Framework, Tech1]`. Missing values: `[tech1]`"),
                Arguments.of("TECh1", true, null, "Required values: `[Framework, Tech1]`. Missing values: `[TECh1]`"),
                Arguments.of("FRAMEWORK", false, EnumValue1.FRAMEWORK, null),
                Arguments.of("framework", true, null, "Required values: `[Framework, Tech1]`. Missing values: `[framework]`"),
                Arguments.of("fraMEwork", true, null, "Required values: `[Framework, Tech1]`. Missing values: `[fraMEwork]`"),
                Arguments.of("Tech2", true, null, "Required values: `[Framework, Tech1]`. Missing values: `[Tech2]`"),
                Arguments.of("Server", true, null, "Required values: `[Framework, Tech1]`. Missing values: `[Server]`"),
                Arguments.of(null, true, null, "Required values: `[Framework, Tech1]`. Missing values: `[null]`")
        );
    }

    private static Stream<Arguments> findEnumByValueOrUnknownArgs() {
        return Stream.of(
                Arguments.of("Tech1", EnumValue2.TECH1),
                Arguments.of("Framework", EnumValue2.FRAMEWORK),
                Arguments.of("123", EnumValue2.UNKNOWN),
                Arguments.of(randomString(), EnumValue2.UNKNOWN)
        );
    }

    private static Stream<Arguments> findEnumByValueIgnoreCaseOrUnknownArgs() {
        return Stream.of(
                Arguments.of("tech1", EnumValue2.TECH1),
                Arguments.of("framework", EnumValue2.FRAMEWORK),
                Arguments.of("TTT", EnumValue2.UNKNOWN),
                Arguments.of(randomString(), EnumValue2.UNKNOWN)
        );
    }

    private static Stream<Arguments> findEnumByNameOrUnknownArgs() {
        return Stream.of(
                Arguments.of("TECH1", EnumValue2.TECH1),
                Arguments.of("FRAMEWORK", EnumValue2.FRAMEWORK),
                Arguments.of("123", EnumValue2.UNKNOWN),
                Arguments.of(randomString(), EnumValue2.UNKNOWN)
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

    @ParameterizedTest
    @MethodSource("findEnumByValueOrUnknownArgs")
    void findEnumByValueOrUnknownTest(String value, EnumValue2 expected) {
        // Act
        var actual = findEnumByValueOrUnknown(EnumValue2.class, value);

        // Assert
        assertThat(actual).isEqualTo(expected);
    }

    @ParameterizedTest
    @MethodSource("findEnumByValueIgnoreCaseOrUnknownArgs")
    void findEnumByValueIgnoreCaseOrUnknownTest(String value, EnumValue2 expected) {
        // Act
        var actual = findEnumByValueIgnoreCaseOrUnknown(EnumValue2.class, value);

        // Assert
        assertThat(actual).isEqualTo(expected);
    }

    @Test
    void findEnumByValueIgnoreCaseOrUnknownFailureTest() {
        // Act
        var throwable = catchThrowable(() -> findEnumByValueIgnoreCaseOrUnknown(EnumValue1.class, "TEST"));

        // Assert
        assertThat(throwable)
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageStartingWith("EnumValue1 does not have UNKNOWN enum value");
    }

    @ParameterizedTest
    @MethodSource("findEnumByNameOrUnknownArgs")
    void findEnumByNameOrUnknownTest(String value, EnumValue2 expected) {
        // Act
        var actual = findEnumByNameOrUnknown(EnumValue2.class, value);

        // Assert
        assertThat(actual).isEqualTo(expected);
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
