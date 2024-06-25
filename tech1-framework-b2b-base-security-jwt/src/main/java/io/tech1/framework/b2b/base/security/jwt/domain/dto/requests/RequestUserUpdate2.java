package io.tech1.framework.b2b.base.security.jwt.domain.dto.requests;

import io.swagger.v3.oas.annotations.media.Schema;
import io.tech1.framework.foundation.domain.constants.ZoneIdsConstants;
import jakarta.validation.constraints.NotNull;

import java.time.ZoneId;

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
}
