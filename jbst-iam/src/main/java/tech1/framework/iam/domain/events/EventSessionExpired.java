package tech1.framework.iam.domain.events;

import tech1.framework.iam.domain.sessions.Session;

public record EventSessionExpired(
        Session session
) {
}
