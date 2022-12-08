package io.tech1.framework.domain.utilities.time;

import io.tech1.framework.domain.tests.constants.TestsConstants;
import io.tech1.framework.domain.time.TimeAmount;
import org.junit.jupiter.api.RepeatedTest;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.stream.Stream;

import static io.tech1.framework.domain.tests.constants.TestsConstants.EET_ZONE_ID;
import static io.tech1.framework.domain.tests.constants.TestsConstants.POLAND_ZONE_ID;
import static io.tech1.framework.domain.utilities.time.TimestampUtility.*;
import static java.time.temporal.ChronoUnit.*;
import static org.assertj.core.api.Assertions.assertThat;

public class TimestampUtilityTest {
    private static final Long _2_HOUR_AGO = getPastRange(TimeAmount.of(2L, HOURS)).getA();
    private static final Long _5_MINUTES_AGO = getPastRange(TimeAmount.of(5L, MINUTES)).getA();
    private static final Long _1_MINUTE_AGO = getPastRange(TimeAmount.of(1L, MINUTES)).getA();
    private static final Long _2_MINUTES_FUTURE = getFutureRange(TimeAmount.of(2L, MINUTES)).getB();
    private static final Long _1_HOUR_FUTURE = getFutureRange(TimeAmount.of(1L, HOURS)).getB();

    private static final long _5_SECONDS = TimeAmount.of(5L, SECONDS).toMillis();

    private static Stream<Arguments> toUnixTimeTest() {
        return Stream.of(
                Arguments.of(1670526412123L, 1670526412L),
                Arguments.of(1670526412456L, 1670526412L),
                Arguments.of(1670526412789L, 1670526412L),
                Arguments.of(1670526412999L, 1670526412L),
                Arguments.of(1670526413001L, 1670526413L)
        );
    }

    private static Stream<Arguments> isBetweenTest() {
        return Stream.of(
                Arguments.of(_5_MINUTES_AGO, _1_HOUR_FUTURE, true),
                Arguments.of(_5_MINUTES_AGO, _1_MINUTE_AGO, false),
                Arguments.of(_1_MINUTE_AGO, _5_MINUTES_AGO, false),
                Arguments.of(_1_HOUR_FUTURE, _5_MINUTES_AGO, false),
                Arguments.of(_1_HOUR_FUTURE, _2_MINUTES_FUTURE, false),
                Arguments.of(_2_MINUTES_FUTURE, _1_HOUR_FUTURE, false),
                Arguments.of(_1_MINUTE_AGO, _5_MINUTES_AGO, false),
                Arguments.of(_1_MINUTE_AGO, _2_HOUR_AGO, false),
                Arguments.of(_1_MINUTE_AGO, _2_MINUTES_FUTURE, true),
                Arguments.of(_1_MINUTE_AGO, _1_HOUR_FUTURE, true)
        );
    }

    private static Stream<Arguments> isPastTest() {
        return Stream.of(
                Arguments.of(1642767625000L, true),
                Arguments.of(1642767626000L, true),
                Arguments.of(getCurrentTimestamp() + 10000L, false)
        );
    }

    private static Stream<Arguments> isFutureTest() {
        return Stream.of(
                Arguments.of(1642767625000L, false),
                Arguments.of(1642767626000L, false),
                Arguments.of(getCurrentTimestamp() + 10000L, true)
        );
    }

    private static Stream<Arguments> isCurrentTimestampNSecondsMoreTest() {
        return Stream.of(
                Arguments.of(getCurrentTimestamp() - _5_SECONDS, 1L, true),
                Arguments.of(getCurrentTimestamp() - _5_SECONDS, 2L, true),
                Arguments.of(getCurrentTimestamp() - _5_SECONDS, 3L, true),
                Arguments.of(getCurrentTimestamp() - _5_SECONDS, 7L, false),
                Arguments.of(getCurrentTimestamp() - _5_SECONDS, 8L, false),
                Arguments.of(getCurrentTimestamp() - _5_SECONDS, 9L, false)
        );
    }

    @RepeatedTest(TestsConstants.RANDOM_ITERATIONS_COUNT)
    public void getCurrentTimestampTest() {
        // Arrange
        var expected = System.currentTimeMillis();

        // Act
        var actual = getCurrentTimestamp();

        // Assert
        assertThat(actual).isGreaterThanOrEqualTo(expected);
    }

    @ParameterizedTest
    @MethodSource("toUnixTimeTest")
    public void toUnixTimeTest(long timestamp, long expected) {
        // Act
        var actual = toUnixTime(timestamp);

        // Assert
        assertThat(actual).isEqualTo(expected);
    }

    @RepeatedTest(TestsConstants.RANDOM_ITERATIONS_COUNT)
    public void getCurrentMonthAtStartOfMonthAndAtStartOfDayTimestampTest() {
        // Act
        var timestampUTC = getCurrentMonthAtStartOfMonthAndAtStartOfDayTimestampUTC();
        var timestampUkraine = getCurrentMonthAtStartOfMonthAndAtStartOfDayTimestamp(EET_ZONE_ID);
        var timestampPoland = getCurrentMonthAtStartOfMonthAndAtStartOfDayTimestamp(POLAND_ZONE_ID);

        // Assert
        assertThat(timestampUTC).isGreaterThan(timestampPoland);
        assertThat(timestampPoland).isGreaterThan(timestampUkraine);
        assertThat(timestampUTC - timestampPoland).isEqualTo(3600000L);
        assertThat(timestampUTC - timestampUkraine).isEqualTo(7200000L);
        assertThat(timestampPoland - timestampUkraine).isEqualTo(3600000L);
    }

    @RepeatedTest(TestsConstants.RANDOM_ITERATIONS_COUNT)
    public void getPreviousMonthAtStartOfMonthAndAtStartOfDayTimestampTest() {
        // Act
        var timestampUTC = getPreviousMonthAtStartOfMonthAndAtStartOfDayTimestampUTC();
        var timestampUkraine = getPreviousMonthAtStartOfMonthAndAtStartOfDayTimestamp(EET_ZONE_ID);
        var timestampPoland = getPreviousMonthAtStartOfMonthAndAtStartOfDayTimestamp(POLAND_ZONE_ID);

        // Assert
        assertThat(timestampUTC).isGreaterThan(timestampPoland);
        assertThat(timestampPoland).isGreaterThan(timestampUkraine);
        assertThat(timestampUTC - timestampPoland).isEqualTo(3600000L);
        assertThat(timestampUTC - timestampUkraine).isEqualTo(7200000L);
        assertThat(timestampPoland - timestampUkraine).isEqualTo(3600000L);
    }

    @RepeatedTest(TestsConstants.RANDOM_ITERATIONS_COUNT)
    public void getNMonthAgoAtStartOfMonthAndAtStartOfDayTimestampTest() {
        // Act
        var timestampUTC = getNMonthAgoAtStartOfMonthAndAtStartOfDayTimestampUTC(4);
        var timestampUkraine = getNMonthAgoAtStartOfMonthAndAtStartOfDayTimestamp(EET_ZONE_ID, 3);
        var timestampPoland = getNMonthAgoAtStartOfMonthAndAtStartOfDayTimestamp(POLAND_ZONE_ID, 3);

        // Assert
        assertThat(timestampUTC).isLessThan(timestampPoland);
        assertThat(timestampUTC).isLessThan(timestampUkraine);
        assertThat(timestampPoland).isGreaterThan(timestampUkraine);
        assertThat(timestampUTC - timestampPoland).isEqualTo(-2671200000L);
        assertThat(timestampUTC - timestampUkraine).isEqualTo(-2667600000L);
        assertThat(timestampPoland - timestampUkraine).isEqualTo(3600000L);
    }

    @ParameterizedTest
    @MethodSource("isBetweenTest")
    public void isBetweenTest(long past, long future, boolean expected) {
        // Act
        var actual = isBetween(getCurrentTimestamp(), past, future);

        // Assert
        assertThat(actual).isEqualTo(expected);
    }

    @ParameterizedTest
    @MethodSource("isPastTest")
    public void isPastTest(long timestamp, boolean expected) {
        // Act
        var actual = isPast(timestamp);

        // Assert
        assertThat(actual).isEqualTo(expected);
    }

    @ParameterizedTest
    @MethodSource("isFutureTest")
    public void isFutureTest(long timestamp, boolean expected) {
        // Act
        var actual = isFuture(timestamp);

        // Assert
        assertThat(actual).isEqualTo(expected);
    }

    @ParameterizedTest
    @MethodSource("isCurrentTimestampNSecondsMoreTest")
    public void isCurrentTimestampNSecondsMoreTest(long timestamp, long seconds, boolean expected) {
        // Act
        var actual = isCurrentTimestampNSecondsMore(timestamp, seconds);

        // Assert
        assertThat(actual).isEqualTo(expected);
    }
}
