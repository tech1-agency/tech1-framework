package io.tech1.framework.domain.tests.constants;

import io.tech1.framework.domain.base.Username;
import lombok.experimental.UtilityClass;

import java.time.ZoneId;
import java.util.TimeZone;

@UtilityClass
public class TestsConstants {
    // TODO [YY] migrate -> TestsDTFs
    // Date Formatters
    public static final String DEFAULT_DATE_FORMAT_PATTERN = "dd.MM.yyyy HH:mm:ss";

    // TODO [YY] migrate -> TestsZoneIdsConstants
    // Poland
    public static final TimeZone POLAND_TIME_ZONE = TimeZone.getTimeZone("Poland");
    public static final ZoneId POLAND_ZONE_ID = ZoneId.of("Poland");

    // Ukraine, Kyiv
    // Daylight Saving Time; EEST: Eastern European Summer Time; UTC+3
    // Standard Time; EET: Eastern European Time; UTC+2
    public static final TimeZone EET_TIME_ZONE = TimeZone.getTimeZone("Europe/Kiev");
    public static final ZoneId EET_ZONE_ID = ZoneId.of("Europe/Kiev");

    // Flags
    // TODO [YY] migrate -> TestsFlagsConstants
    public static final String FLAG_UKRAINE = "🇺🇦";
    public static final String FLAG_PORTUGAL = "🇵🇹";
    public static final String FLAG_UK = "🇬🇧";
    public static final String FLAG_USA = "🇺🇸";

    // Username
    // TODO [YY] migrate -> TestsUsernamesConstants
    public static final Username TECH1 = Username.of("tech1");
}
