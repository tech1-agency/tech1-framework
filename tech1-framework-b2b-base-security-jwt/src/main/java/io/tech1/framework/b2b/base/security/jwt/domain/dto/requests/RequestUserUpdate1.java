package io.tech1.framework.b2b.base.security.jwt.domain.dto.requests;

import io.tech1.framework.domain.base.Email;
import io.tech1.framework.domain.constants.ZoneIdsConstants;

import jakarta.validation.constraints.NotNull;
import java.time.ZoneId;

public record RequestUserUpdate1(
        @NotNull ZoneId zoneId,
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
