package jbst.foundation.utilities.enums;

import jbst.foundation.domain.tests.enums.EnumValue1;
import jbst.foundation.domain.tests.enums.EnumValue2;
import jbst.foundation.domain.tests.enums.EnumValue3;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.stream.Stream;

import static jbst.foundation.domain.tests.enums.EnumValue2.UNKNOWN;
import static jbst.foundation.utilities.enums.EnumCreatorUtility.*;
import static jbst.foundation.utilities.random.RandomUtility.randomString;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.catchThrowable;

class EnumCreatorUtilityTest {

    private static Stream<Arguments> findEnumByValueIgnoreCaseOrThrowArgs() {
        return Stream.of(
                Arguments.of("JBst", false, EnumValue1.JBST, null),
                Arguments.of("jbst", false, EnumValue1.JBST, null),
                Arguments.of("jbST", false, EnumValue1.JBST, null),
                Arguments.of("Framework", false, EnumValue1.FRAMEWORK, null),
                Arguments.of("framework", false, EnumValue1.FRAMEWORK, null),
                Arguments.of("fraMEwork", false, EnumValue1.FRAMEWORK, null),
                Arguments.of("jbst2", true, null, "Options: `[Framework, jbst]`. Unexpected: `[jbst2]`"),
                Arguments.of("Server", true, null, "Options: `[Framework, jbst]`. Unexpected: `[Server]`"),
                Arguments.of(null, true, null, "Options: `[Framework, jbst]`. Unexpected: `[null]`")
        );
    }

    private static Stream<Arguments> findEnumByNameOrThrowArgs() {
        return Stream.of(
                Arguments.of("jbst", false, EnumValue1.JBST, null),
                Arguments.of("jbST", true, null, "Options: `[Framework, jbst]`. Unexpected: `[jbST]`"),
                Arguments.of("JBst", true, null, "Options: `[Framework, jbst]`. Unexpected: `[JBst]`"),
                Arguments.of("FRAMEWORK", false, EnumValue1.FRAMEWORK, null),
                Arguments.of("framework", true, null, "Options: `[Framework, jbst]`. Unexpected: `[framework]`"),
                Arguments.of("fraMEwork", true, null, "Options: `[Framework, jbst]`. Unexpected: `[fraMEwork]`"),
                Arguments.of("jbst2", true, null, "Options: `[Framework, jbst]`. Unexpected: `[jbst2]`"),
                Arguments.of("Server", true, null, "Options: `[Framework, jbst]`. Unexpected: `[Server]`"),
                Arguments.of(null, true, null, "Options: `[Framework, jbst]`. Unexpected: `[null]`")
        );
    }

    private static Stream<Arguments> findEnumByValueOrUnknownArgs() {
        return Stream.of(
                Arguments.of("jbst", EnumValue2.JBST),
                Arguments.of("Framework", EnumValue2.FRAMEWORK),
                Arguments.of("123", EnumValue2.UNKNOWN),
                Arguments.of(randomString(), EnumValue2.UNKNOWN),
                Arguments.of(null, EnumValue2.UNKNOWN)
        );
    }

    private static Stream<Arguments> findEnumByValueIgnoreCaseOrUnknownArgs() {
        return Stream.of(
                Arguments.of("JBST", EnumValue2.JBST),
                Arguments.of("framework", EnumValue2.FRAMEWORK),
                Arguments.of("TTT", EnumValue2.UNKNOWN),
                Arguments.of(randomString(), EnumValue2.UNKNOWN),
                Arguments.of(null, EnumValue2.UNKNOWN)
        );
    }

    private static Stream<Arguments> findEnumByIntegerValueTest() {
        return Stream.of(
                Arguments.of(0, EnumValue3.EMAIL_SENT),
                Arguments.of(1, EnumValue3.CANCELLED),
                Arguments.of(2, EnumValue3.AWAITING_APPROVAL),
                Arguments.of(3, EnumValue3.REJECTED),
                Arguments.of(4, EnumValue3.PROCESSING),
                Arguments.of(5, EnumValue3.FAILURE),
                Arguments.of(6, EnumValue3.COMPLETED),
                Arguments.of(7, EnumValue3.UNKNOWN),
                Arguments.of(999, EnumValue3.UNKNOWN)
        );
    }

    private static Stream<Arguments> findEnumByNameOrUnknownArgs() {
        return Stream.of(
                Arguments.of("JBST", EnumValue2.JBST),
                Arguments.of("FRAMEWORK", EnumValue2.FRAMEWORK),
                Arguments.of("123", EnumValue2.UNKNOWN),
                Arguments.of(randomString(), EnumValue2.UNKNOWN),
                Arguments.of(null, EnumValue2.UNKNOWN)
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
    @MethodSource("findEnumByIntegerValueTest")
    void findEnumByValueOrUnknownTest(int value, EnumValue3 expected) {
        // Act
        var actual = findEnumByValueOrUnknown(EnumValue3.class, value);

        // Assert
        assertThat(actual).isEqualTo(expected);
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
