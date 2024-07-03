package io.tech1.framework.iam.domain.events;

import io.tech1.framework.iam.domain.sessions.Session;

public record EventSessionRefreshed(
        Session session
) {
}
