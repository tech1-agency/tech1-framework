package io.tech1.framework.domain.tests.constants;

import lombok.experimental.UtilityClass;

import java.time.ZoneId;
import java.util.TimeZone;

@UtilityClass
public class TestsConstants {
    public final static int SMALL_ITERATIONS_COUNT = 10;
    public final static int RANDOM_ITERATIONS_COUNT = 100;

    // Date Formatters
    public static final String DEFAULT_DATE_FORMAT_PATTERN = "dd.MM.yyyy HH:mm:ss";

    // UK, London
    public static final TimeZone UTC_TIME_ZONE = TimeZone.getTimeZone("UTC");
    public static final ZoneId UTC_ZONE_ID = ZoneId.of("UTC");

    // Poland
    public static final TimeZone POLAND_TIME_ZONE = TimeZone.getTimeZone("Poland");
    public static final ZoneId POLAND_ZONE_ID = ZoneId.of("Poland");

    // Ukraine, Kyiv
    // Daylight Saving Time; EEST: Eastern European Summer Time; UTC+3
    // Standard Time; EET: Eastern European Time; UTC+2
    public static final TimeZone EET_TIME_ZONE = TimeZone.getTimeZone("Europe/Kiev");
    public static final ZoneId EET_ZONE_ID = ZoneId.of("Europe/Kiev");
}
