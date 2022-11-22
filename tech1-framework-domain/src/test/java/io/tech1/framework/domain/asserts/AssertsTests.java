package io.tech1.framework.domain.asserts;

import io.tech1.framework.domain.reflections.ClassProperty;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.Collection;
import java.util.List;
import java.util.stream.Stream;

import static io.tech1.framework.domain.asserts.Asserts.*;
import static io.tech1.framework.domain.utilities.random.RandomUtility.randomString;
import static java.util.Collections.emptyList;
import static java.util.Collections.emptySet;
import static java.util.Objects.isNull;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.catchThrowable;

public class AssertsTests {

    // =================================================================================================================
    // 1 assert complexity
    // =================================================================================================================
    private static Stream<Arguments> assertNonNullOrThrowTest() {
        return Stream.of(
                Arguments.of(new Object(), null),
                Arguments.of(null, randomString())
        );
    }

    private static Stream<Arguments> assertNonNullClassPropertyOrThrowTest() {
        return Stream.of(
                Arguments.of(null, "Unknown class property"),
                Arguments.of(new ClassProperty(randomString(), "attribute1", null), "Attribute `attribute1` is invalid"),
                Arguments.of(new ClassProperty(randomString(), "attribute1", new Object()), null)
        );
    }

    private static Stream<Arguments> assertNonBlankOrThrowTest() {
        return Stream.of(
                Arguments.of(randomString(), null),
                Arguments.of("", randomString()),
                Arguments.of(" ", randomString())
        );
    }

    private static Stream<Arguments> assertNonEmptyOrThrowTest() {
        return Stream.of(
                Arguments.of(List.of(randomString(), randomString()), null),
                Arguments.of(emptyList(), randomString()),
                Arguments.of(emptySet(), randomString())
        );
    }

    private static Stream<Arguments> assertTrueOrThrowTest() {
        return Stream.of(
                Arguments.of(true, null),
                Arguments.of(false, randomString()),
                Arguments.of(false, randomString())
        );
    }

    private static Stream<Arguments> assertFalseOrThrowTest() {
        return Stream.of(
                Arguments.of(false, null),
                Arguments.of(true, randomString()),
                Arguments.of(true, randomString())
        );
    }

    private static Stream<Arguments> requireNonNullOrThrowTest() {
        return Stream.of(
                Arguments.of(randomString(), null),
                Arguments.of(null, randomString())
        );
    }

    // =================================================================================================================
    // 1+ asserts complexity
    // =================================================================================================================
    private static Stream<Arguments> assertNonNullNotBlankOrThrowTest() {
        return Stream.of(
                Arguments.of(new Object(), null),
                Arguments.of(null, randomString()),
                Arguments.of("", randomString()),
                Arguments.of("  ", randomString())
        );
    }

    private static Stream<Arguments> assertNonNullNotEmptyOrThrowTest() {
        return Stream.of(
                Arguments.of(List.of(randomString(), randomString()), null),
                Arguments.of(null, randomString()),
                Arguments.of(emptyList(), randomString()),
                Arguments.of(emptySet(), randomString())
        );
    }

    private static Stream<Arguments> assertZoneIdOrThrowTest() {
        return Stream.of(
                Arguments.of("", randomString()),
                Arguments.of("Europe/Kiev1", randomString()),
                Arguments.of("Europe/Kiev2", randomString()),
                Arguments.of("Europe/Kiev", null)
        );
    }

    private static Stream<Arguments> assertDateTimePatternOrThrowTest() {
        return Stream.of(
                Arguments.of("", randomString()),
                Arguments.of("notFormatter1", "Unknown pattern letter: o"),
                Arguments.of("Europe/Kiev2", "Unknown pattern letter: r"),
                Arguments.of("dd.MM.yyyy HH:mm:ss", null),
                Arguments.of("dd/MM HH.mm", null)
        );
    }

    // =================================================================================================================
    // 1 assert complexity
    // =================================================================================================================
    @ParameterizedTest
    @MethodSource("assertNonNullOrThrowTest")
    public void assertNonNullOrThrowTest(Object object, String expectedErrorMessage) {
        // Act
        var throwable = catchThrowable(() -> assertNonNullOrThrow(object, expectedErrorMessage));

        // Assert
        if (isNull(expectedErrorMessage)) {
            assertThat(throwable).isNull();
        } else {
            assertThat(throwable).isNotNull();
            assertThat(throwable).isInstanceOf(IllegalArgumentException.class);
            assertThat(throwable.getMessage()).isEqualTo(expectedErrorMessage);
        }
    }

    @ParameterizedTest
    @MethodSource("assertNonNullClassPropertyOrThrowTest")
    public void assertNonNullClassPropertyOrThrowTest(ClassProperty classProperty, String expectedErrorMessage) {
        // Act
        var throwable = catchThrowable(() -> assertNonNullClassPropertyOrThrow(classProperty));

        // Assert
        if (isNull(expectedErrorMessage)) {
            assertThat(throwable).isNull();
        } else {
            assertThat(throwable).isNotNull();
            assertThat(throwable).isInstanceOf(IllegalArgumentException.class);
            assertThat(throwable.getMessage()).isEqualTo(expectedErrorMessage);
        }
    }

    @ParameterizedTest
    @MethodSource("assertNonBlankOrThrowTest")
    public void assertNonBlankOrThrowTest(String object, String expectedErrorMessage) {
        // Act
        var throwable = catchThrowable(() -> assertNonBlankOrThrow(object, expectedErrorMessage));

        // Assert
        if (isNull(expectedErrorMessage)) {
            assertThat(throwable).isNull();
        } else {
            assertThat(throwable).isNotNull();
            assertThat(throwable).isInstanceOf(IllegalArgumentException.class);
            assertThat(throwable.getMessage()).isEqualTo(expectedErrorMessage);
        }
    }

    @ParameterizedTest
    @MethodSource("assertNonEmptyOrThrowTest")
    public void assertNonEmptyOrThrowTest(Collection<?> collection, String expectedErrorMessage) {
        // Act
        var throwable = catchThrowable(() -> assertNonEmptyOrThrow(collection, expectedErrorMessage));

        // Assert
        if (isNull(expectedErrorMessage)) {
            assertThat(throwable).isNull();
        } else {
            assertThat(throwable).isNotNull();
            assertThat(throwable).isInstanceOf(IllegalArgumentException.class);
            assertThat(throwable.getMessage()).isEqualTo(expectedErrorMessage);
        }
    }

    @ParameterizedTest
    @MethodSource("assertTrueOrThrowTest")
    public void assertTrueOrThrowTest(boolean flag, String expectedErrorMessage) {
        // Act
        var throwable = catchThrowable(() -> assertTrueOrThrow(flag, expectedErrorMessage));

        // Assert
        if (isNull(expectedErrorMessage)) {
            assertThat(throwable).isNull();
        } else {
            assertThat(throwable).isNotNull();
            assertThat(throwable).isInstanceOf(IllegalArgumentException.class);
            assertThat(throwable.getMessage()).isEqualTo(expectedErrorMessage);
        }
    }

    @ParameterizedTest
    @MethodSource("assertFalseOrThrowTest")
    public void assertFalseOrThrowTest(boolean flag, String expectedErrorMessage) {
        // Act
        var throwable = catchThrowable(() -> assertFalseOrThrow(flag, expectedErrorMessage));

        // Assert
        if (isNull(expectedErrorMessage)) {
            assertThat(throwable).isNull();
        } else {
            assertThat(throwable).isNotNull();
            assertThat(throwable).isInstanceOf(IllegalArgumentException.class);
            assertThat(throwable.getMessage()).isEqualTo(expectedErrorMessage);
        }
    }

    @ParameterizedTest
    @MethodSource("requireNonNullOrThrowTest")
    public void requireNonNullOrThrowTest(Object object, String expectedErrorMessage) {
        // Act
        var throwable = catchThrowable(() -> {
            // Act
            var actual = requireNonNullOrThrow(object, expectedErrorMessage);

            // Assert
            assertThat(actual).isEqualTo(object);
        });

        // Assert
        if (isNull(expectedErrorMessage)) {
            assertThat(throwable).isNull();
        } else {
            assertThat(throwable).isNotNull();
            assertThat(throwable).isInstanceOf(IllegalArgumentException.class);
            assertThat(throwable.getMessage()).isEqualTo(expectedErrorMessage);
        }
    }

    // =================================================================================================================
    // 1+ asserts complexity
    // =================================================================================================================
    @ParameterizedTest
    @MethodSource("assertNonNullNotBlankOrThrowTest")
    public void assertNonNullNotBlankOrThrowTest(Object object, String expectedErrorMessage) {
        // Act
        var throwable = catchThrowable(() -> assertNonNullNotBlankOrThrow(object, expectedErrorMessage));

        // Assert
        if (isNull(expectedErrorMessage)) {
            assertThat(throwable).isNull();
        } else {
            assertThat(throwable).isNotNull();
            assertThat(throwable).isInstanceOf(IllegalArgumentException.class);
            assertThat(throwable.getMessage()).isEqualTo(expectedErrorMessage);
        }
    }

    @ParameterizedTest
    @MethodSource("assertNonNullNotEmptyOrThrowTest")
    public void assertNonNullNotEmptyOrThrowTest(Collection<?> collection, String expectedErrorMessage) {
        // Act
        var throwable = catchThrowable(() -> assertNonNullNotEmptyOrThrow(collection, expectedErrorMessage));

        // Assert
        if (isNull(expectedErrorMessage)) {
            assertThat(throwable).isNull();
        } else {
            assertThat(throwable).isNotNull();
            assertThat(throwable).isInstanceOf(IllegalArgumentException.class);
            assertThat(throwable.getMessage()).isEqualTo(expectedErrorMessage);
        }
    }

    @ParameterizedTest
    @MethodSource("assertZoneIdOrThrowTest")
    public void assertZoneIdOrThrowTest(String zoneId, String expectedErrorMessage) {
        // Act
        var throwable = catchThrowable(() -> assertZoneIdOrThrow(zoneId, expectedErrorMessage));

        // Assert
        if (isNull(expectedErrorMessage)) {
            assertThat(throwable).isNull();
        } else {
            assertThat(throwable).isNotNull();
            assertThat(throwable).isInstanceOf(IllegalArgumentException.class);
            assertThat(throwable.getMessage()).isEqualTo(expectedErrorMessage);
        }
    }

    @ParameterizedTest
    @MethodSource("assertDateTimePatternOrThrowTest")
    public void assertDateTimePatternOrThrowTest(String dateTimePattern, String expectedErrorMessage) {
        // Act
        var throwable = catchThrowable(() -> assertDateTimePatternOrThrow(dateTimePattern, expectedErrorMessage));

        // Assert
        if (isNull(expectedErrorMessage)) {
            assertThat(throwable).isNull();
        } else {
            assertThat(throwable).isNotNull();
            assertThat(throwable).isInstanceOf(IllegalArgumentException.class);
            assertThat(throwable.getMessage()).isEqualTo(expectedErrorMessage);
        }
    }
}
