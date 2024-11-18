package jbst.foundation.domain.constants;

import lombok.experimental.UtilityClass;

import java.time.ZoneId;

@UtilityClass
public class ZoneIdsConstants {
    // Poland
    public static final ZoneId POLAND = ZoneId.of("Poland");

    // Ukraine, Kyiv
    // Daylight Saving Time; EEST: Eastern European Summer Time; UTC+3
    // Standard Time; EET: Eastern European Time; UTC+2
    // WARNING: https://github.com/eggert/tz/commit/e13e9c531fc48a04fb8d064acccc9f8ae68d5544
    public static final ZoneId UKRAINE = ZoneId.of("Europe/Kyiv");
    @Deprecated(forRemoval = true, since = "2.8.0")
    public static final ZoneId UKRAINE_LEGACY = ZoneId.of("Europe/Kiev");
}
