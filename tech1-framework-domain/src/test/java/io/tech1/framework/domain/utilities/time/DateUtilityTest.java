package io.tech1.framework.domain.utilities.time;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.util.concurrent.TimeUnit;
import java.util.stream.Stream;

import static io.tech1.framework.domain.constants.ZoneIdsConstants.UKRAINE;
import static io.tech1.framework.domain.tests.constants.TestsDTFsConstants.DEFAULT_DATE_FORMAT_PATTERN;
import static io.tech1.framework.domain.utilities.time.DateUtility.convertLocalDateTime;
import static io.tech1.framework.domain.utilities.time.DateUtility.getAbsDifferenceByTimeUnit;
import static java.time.LocalDateTime.of;
import static java.time.Month.DECEMBER;
import static java.util.TimeZone.getTimeZone;
import static org.assertj.core.api.Assertions.assertThat;

class DateUtilityTest {
    private static final SimpleDateFormat SDF = new SimpleDateFormat(DEFAULT_DATE_FORMAT_PATTERN);

    private static Stream<Arguments> convertLocalDateTimeTest() {
        return Stream.of(
                Arguments.of(of(2021, DECEMBER, 25, 15, 16, 17), "25.12.2021 15:16:17"),
                Arguments.of(of(2011, DECEMBER, 25, 15, 16, 17), "25.12.2011 15:16:17"),
                Arguments.of(of(2001, DECEMBER, 25, 15, 16, 17), "25.12.2001 15:16:17")
        );
    }

    @ParameterizedTest
    @MethodSource("convertLocalDateTimeTest")
    void convertLocalDateTimeTest(LocalDateTime localDateTime, String expected) throws ParseException {
        // Arrange
        SDF.setTimeZone(getTimeZone(UKRAINE));

        // Act
        var actual = convertLocalDateTime(localDateTime, UKRAINE);

        // Assert
        assertThat(actual).isEqualTo(SDF.parse(expected));
    }

    private static Stream<Arguments> getAbsDifferenceByTimeUnitTest() {
        return Stream.of(
                Arguments.of("25.12.2021 15:16:17", "25.12.2011 15:16:17", TimeUnit.DAYS, 3653L),
                Arguments.of("25.12.2021 15:16:17", "25.12.2011 15:16:17", TimeUnit.MINUTES, 5260320L),
                Arguments.of("25.12.2021 15:16:17", "25.12.2021 16:00:00", TimeUnit.DAYS, 0L),
                Arguments.of("25.12.2021 15:16:17", "25.12.2021 16:00:00", TimeUnit.HOURS, 0L),
                Arguments.of("25.12.2021 15:16:17", "25.12.2021 16:00:00", TimeUnit.MINUTES, 43L),
                Arguments.of("25.12.2021 15:16:17", "25.12.2021 16:00:00", TimeUnit.SECONDS, 2623L)
        );
    }

    @ParameterizedTest
    @MethodSource("getAbsDifferenceByTimeUnitTest")
    void getAbsDifferenceByTimeUnitTest(String date1, String date2, TimeUnit timeUnit, long expected) throws ParseException {
        // Act
        var actual = getAbsDifferenceByTimeUnit(SDF.parse(date1), SDF.parse(date2), timeUnit);

        // Assert
        assertThat(actual).isEqualTo(expected);
    }
}
