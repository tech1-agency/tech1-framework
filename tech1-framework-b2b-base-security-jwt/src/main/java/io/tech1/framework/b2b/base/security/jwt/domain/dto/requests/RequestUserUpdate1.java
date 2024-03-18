package io.tech1.framework.b2b.base.security.jwt.domain.dto.requests;

import io.tech1.framework.domain.base.Email;
import io.tech1.framework.domain.constants.ZoneIdsConstants;

public record RequestUserUpdate1(
        String zoneId,
        Email email,
        String name
) {

    public static RequestUserUpdate1 testsHardcoded() {
        return new RequestUserUpdate1(
                ZoneIdsConstants.UKRAINE.getId(),
                Email.testsHardcoded(),
                "Tech1 Ops"
        );
    }
}
