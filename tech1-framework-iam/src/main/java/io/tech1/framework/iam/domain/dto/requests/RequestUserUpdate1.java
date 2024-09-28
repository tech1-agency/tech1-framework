package io.tech1.framework.iam.domain.dto.requests;

import io.swagger.v3.oas.annotations.media.Schema;
import tech1.framework.foundation.domain.base.Email;
import tech1.framework.foundation.domain.constants.ZoneIdsConstants;
import jakarta.validation.constraints.NotNull;

import java.time.ZoneId;

import static tech1.framework.foundation.utilities.zones.ZonesUtility.reworkUkraineZoneId;

public record RequestUserUpdate1(
        @Schema(type = "string") @NotNull ZoneId zoneId,
        @Email.ValidEmail Email email,
        String name
) {

    public static RequestUserUpdate1 testsHardcoded() {
        return new RequestUserUpdate1(
                ZoneIdsConstants.UKRAINE,
                Email.testsHardcoded(),
                "Tech1 Ops"
        );
    }

    public RequestUserUpdate1 createReworkedUkraineZoneId() {
        return new RequestUserUpdate1(
                reworkUkraineZoneId(this.zoneId),
                this.email,
                this.name
        );
    }
}
