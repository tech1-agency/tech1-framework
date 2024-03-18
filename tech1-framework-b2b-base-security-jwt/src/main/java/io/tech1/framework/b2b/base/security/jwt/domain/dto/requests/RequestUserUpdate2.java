package io.tech1.framework.b2b.base.security.jwt.domain.dto.requests;

import io.tech1.framework.domain.constants.ZoneIdsConstants;

public record RequestUserUpdate2(
        String zoneId,
        String name
) {

    public static RequestUserUpdate2 testsHardcoded() {
        return new RequestUserUpdate2(
                ZoneIdsConstants.UKRAINE.getId(),
                "Tech1 Ops"
        );
    }
}
