package tech1.framework.iam.sessions;

import tech1.framework.iam.events.publishers.SecurityJwtIncidentPublisher;
import tech1.framework.iam.events.publishers.SecurityJwtPublisher;
import tech1.framework.iam.repositories.mongodb.MongoUsersSessionsRepository;
import tech1.framework.iam.services.mongodb.MongoBaseUsersSessionsService;

public class MongoSessionRegistry extends AbstractSessionRegistry {

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
