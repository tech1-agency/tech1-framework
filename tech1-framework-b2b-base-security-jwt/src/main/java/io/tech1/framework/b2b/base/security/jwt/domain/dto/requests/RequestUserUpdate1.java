package io.tech1.framework.b2b.base.security.jwt.domain.dto.requests;

import io.swagger.v3.oas.annotations.media.Schema;
import io.tech1.framework.foundation.domain.base.Email;
import io.tech1.framework.foundation.domain.constants.ZoneIdsConstants;
import jakarta.validation.constraints.NotNull;

import java.time.ZoneId;

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
}
