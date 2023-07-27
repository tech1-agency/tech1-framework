package io.tech1.framework.b2b.mongodb.security.jwt.domain.events;

import io.tech1.framework.b2b.base.security.jwt.domain.sessions.Session;

public record EventSessionExpired(
        Session session
) {
}
