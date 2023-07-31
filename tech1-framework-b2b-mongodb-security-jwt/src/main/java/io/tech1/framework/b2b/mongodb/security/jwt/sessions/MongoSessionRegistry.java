package io.tech1.framework.b2b.mongodb.security.jwt.sessions;

import io.tech1.framework.b2b.base.security.jwt.events.publishers.SecurityJwtIncidentPublisher;
import io.tech1.framework.b2b.base.security.jwt.events.publishers.SecurityJwtPublisher;
import io.tech1.framework.b2b.base.security.jwt.services.BaseUsersSessionsService;
import io.tech1.framework.b2b.base.security.jwt.sessions.AbstractSessionRegistry;
import io.tech1.framework.b2b.mongodb.security.jwt.repositories.MongoUsersSessionsRepository;
import io.tech1.framework.b2b.mongodb.security.jwt.services.MongoBaseUsersSessionsService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@SuppressWarnings("SpringJavaInjectionPointsAutowiringInspection")
@Slf4j
@Service
public class MongoSessionRegistry extends AbstractSessionRegistry {

    @Autowired
    public MongoSessionRegistry(
            SecurityJwtPublisher securityJwtPublisher,
            SecurityJwtIncidentPublisher securityJwtIncidentPublisher,
            MongoBaseUsersSessionsService baseUsersSessionsService,
            MongoUsersSessionsRepository usersSessionsRepository
    ) {
        super(
                securityJwtPublisher,
                securityJwtIncidentPublisher,
                baseUsersSessionsService,
                usersSessionsRepository
        );
    }
}
