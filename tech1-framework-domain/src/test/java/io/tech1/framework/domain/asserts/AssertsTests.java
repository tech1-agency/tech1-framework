package io.tech1.framework.domain.asserts;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.stream.Stream;

import static io.tech1.framework.domain.asserts.Asserts.assertNonNullOrThrow;
import static java.util.Objects.isNull;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.catchThrowable;

public class AssertsTests {

    private static Stream<Arguments> assertNonNullOrThrowTestParameters() {
        return Stream.of(
                Arguments.of(new Object(), null),
                Arguments.of(null, "errorMessage")
        );
    }

    @ParameterizedTest
    @MethodSource("assertNonNullOrThrowTestParameters")
    public void assertNonNullOrThrowTest(Object object, String expectedErrorMessage) {
        // Act
        var throwable = catchThrowable(() -> assertNonNullOrThrow(object, expectedErrorMessage));

        // Assert
        if (isNull(expectedErrorMessage)) {
            assertThat(throwable).isNull();
        } else {
            assertThat(throwable).isNotNull();
            assertThat(throwable).isInstanceOf(IllegalArgumentException.class);
            assertThat(throwable).hasMessageContaining(expectedErrorMessage);
        }
    }
}
