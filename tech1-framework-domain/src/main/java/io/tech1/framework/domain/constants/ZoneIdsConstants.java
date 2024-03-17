package io.tech1.framework.domain.constants;

import lombok.experimental.UtilityClass;

import java.time.ZoneId;

@UtilityClass
public class ZoneIdsConstants {
    // Poland
    public static final ZoneId POLAND_ZONE_ID = ZoneId.of("Poland");

    // Ukraine, Kyiv
    // Daylight Saving Time; EEST: Eastern European Summer Time; UTC+3
    // Standard Time; EET: Eastern European Time; UTC+2
    public static final ZoneId EET_ZONE_ID = ZoneId.of("Europe/Kiev");
}
