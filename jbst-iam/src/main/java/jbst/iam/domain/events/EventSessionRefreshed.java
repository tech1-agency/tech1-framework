package jbst.iam.domain.events;

import jbst.iam.domain.sessions.Session;

public record EventSessionRefreshed(
        Session session
) {
}
