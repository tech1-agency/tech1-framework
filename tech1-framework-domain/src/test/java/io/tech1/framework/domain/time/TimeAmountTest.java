package io.tech1.framework.domain.time;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.stream.Stream;

import static java.time.temporal.ChronoUnit.*;
import static org.assertj.core.api.Assertions.assertThat;

class TimeAmountTest {

    private static Stream<Arguments> toTest() {
        return Stream.of(
                Arguments.of(TimeAmount.of(10L, SECONDS), 10L, 10000L),
                Arguments.of(TimeAmount.of(10L, MINUTES), 600L, 600000L),
                Arguments.of(TimeAmount.of(10L, HOURS), 36000L, 36000000L),
                Arguments.of(TimeAmount.of(10L, DAYS), 864000L, 864000000L)
        );
    }

    @ParameterizedTest
    @MethodSource("toTest")
    void toTest(TimeAmount timeAmount, long expectedSeconds, long expectedMillis) {
        // Act
        var actualSeconds = timeAmount.toSeconds();
        var actualMillis = timeAmount.toMillis();

        // Assert
        assertThat(actualSeconds).isEqualTo(expectedSeconds);
        assertThat(actualMillis).isEqualTo(expectedMillis);
    }
}
