package jbst.iam.sessions;

import jbst.iam.events.publishers.SecurityJwtIncidentPublisher;
import jbst.iam.events.publishers.SecurityJwtPublisher;
import jbst.iam.repositories.mongodb.MongoUsersSessionsRepository;
import jbst.iam.services.mongodb.MongoBaseUsersSessionsService;

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
