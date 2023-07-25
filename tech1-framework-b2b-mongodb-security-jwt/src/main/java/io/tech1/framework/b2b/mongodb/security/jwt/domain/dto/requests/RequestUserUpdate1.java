package io.tech1.framework.b2b.mongodb.security.jwt.domain.dto.requests;

import io.tech1.framework.domain.base.Email;

public record RequestUserUpdate1(
        String zoneId,
        Email email,
        String name
) {
}
