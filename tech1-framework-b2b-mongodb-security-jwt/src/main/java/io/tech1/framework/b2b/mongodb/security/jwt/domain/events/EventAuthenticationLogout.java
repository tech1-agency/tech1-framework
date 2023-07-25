package io.tech1.framework.b2b.mongodb.security.jwt.domain.events;

import io.tech1.framework.b2b.mongodb.security.jwt.domain.session.Session;

public record EventAuthenticationLogout(
        Session session
) {
}
