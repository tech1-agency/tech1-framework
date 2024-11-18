package jbst.iam.domain.dto.requests;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import jbst.foundation.domain.constants.ZoneIdsConstants;

import java.time.ZoneId;

import static jbst.foundation.utilities.zones.ZonesUtility.reworkUkraineZoneId;

public record RequestUserUpdate2(
        @Schema(type = "string") @NotNull ZoneId zoneId,
        String name
) {

    public static RequestUserUpdate2 hardcoded() {
        return new RequestUserUpdate2(
                ZoneIdsConstants.UKRAINE,
                "jbst"
        );
    }

    public RequestUserUpdate2 createReworkedUkraineZoneId() {
        return new RequestUserUpdate2(
                reworkUkraineZoneId(this.zoneId),
                this.name
        );
    }
}
