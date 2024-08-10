package io.tech1.framework.foundation.utilities.zones;

import io.tech1.framework.foundation.domain.constants.ZoneIdsConstants;
import lombok.experimental.UtilityClass;
import org.jetbrains.annotations.NotNull;

import java.time.ZoneId;

@UtilityClass
public class ZonesUtility {

    public static ZoneId reworkUkraineZoneId(@NotNull ZoneId zoneId) {
        if ("Europe/Kiev".equals(zoneId.getId())) {
            return ZoneIdsConstants.UKRAINE;
        }
        return zoneId;
    }
}
