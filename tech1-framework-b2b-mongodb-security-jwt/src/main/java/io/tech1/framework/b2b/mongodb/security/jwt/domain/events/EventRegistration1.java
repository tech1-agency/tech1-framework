package io.tech1.framework.b2b.mongodb.security.jwt.domain.events;

import io.tech1.framework.b2b.base.security.jwt.domain.dto.requests.RequestUserRegistration1;

public record EventRegistration1(
        RequestUserRegistration1 requestUserRegistration1
) {
}
