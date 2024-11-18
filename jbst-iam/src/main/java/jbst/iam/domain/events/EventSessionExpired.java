package jbst.iam.domain.events;

import jbst.iam.domain.sessions.Session;

public record EventSessionExpired(
        Session session
) {
}
