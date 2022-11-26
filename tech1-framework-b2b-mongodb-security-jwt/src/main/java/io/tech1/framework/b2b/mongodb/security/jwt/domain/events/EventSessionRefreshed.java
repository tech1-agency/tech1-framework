package io.tech1.framework.b2b.mongodb.security.jwt.domain.events;

import io.tech1.framework.b2b.mongodb.security.jwt.domain.session.Session;
import lombok.Data;

// Lombok
@Data(staticConstructor = "of")
public class EventSessionRefreshed {
    private final Session session;
}
