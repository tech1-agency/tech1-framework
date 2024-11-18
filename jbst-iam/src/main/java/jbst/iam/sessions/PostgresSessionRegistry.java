package jbst.iam.sessions;

import jbst.iam.events.publishers.SecurityJwtIncidentPublisher;
import jbst.iam.events.publishers.SecurityJwtPublisher;
import jbst.iam.repositories.postgres.PostgresUsersSessionsRepository;
import jbst.iam.services.postgres.PostgresBaseUsersSessionsService;

public class PostgresSessionRegistry extends AbstractSessionRegistry {

    public PostgresSessionRegistry(
            SecurityJwtPublisher securityJwtPublisher,
            SecurityJwtIncidentPublisher securityJwtIncidentPublisher,
            PostgresBaseUsersSessionsService baseUsersSessionsService,
            PostgresUsersSessionsRepository usersSessionsRepository
    ) {
        super(
                securityJwtPublisher,
                securityJwtIncidentPublisher,
                baseUsersSessionsService,
                usersSessionsRepository
        );
    }
}
