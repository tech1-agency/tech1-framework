package io.tech1.framework.b2b.base.security.jwt.domain.dto.requests;

import io.tech1.framework.domain.constants.ZoneIdsConstants;

import javax.validation.constraints.NotNull;
import java.time.ZoneId;

public record RequestUserUpdate2(
        @NotNull ZoneId zoneId,
        String name
) {

    public static RequestUserUpdate2 testsHardcoded() {
        return new RequestUserUpdate2(
                ZoneIdsConstants.UKRAINE,
                "Tech1 Ops"
        );
    }
}
