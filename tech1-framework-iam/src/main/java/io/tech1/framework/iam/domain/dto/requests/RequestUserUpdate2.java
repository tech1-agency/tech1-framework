package io.tech1.framework.iam.domain.dto.requests;

import io.swagger.v3.oas.annotations.media.Schema;
import io.tech1.framework.foundation.domain.constants.ZoneIdsConstants;
import jakarta.validation.constraints.NotNull;

import java.time.ZoneId;

import static io.tech1.framework.foundation.utilities.zones.ZonesUtility.reworkUkraineZoneId;

public record RequestUserUpdate2(
        @Schema(type = "string") @NotNull ZoneId zoneId,
        String name
) {

    public static RequestUserUpdate2 testsHardcoded() {
        return new RequestUserUpdate2(
                ZoneIdsConstants.UKRAINE,
                "Tech1 Ops"
        );
    }

    public RequestUserUpdate2 createReworkedUkraineZoneId() {
        return new RequestUserUpdate2(
                reworkUkraineZoneId(this.zoneId),
                this.name
        );
    }
}
