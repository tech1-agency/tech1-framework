package io.tech1.framework.iam.sessions;

import io.tech1.framework.iam.events.publishers.SecurityJwtIncidentPublisher;
import io.tech1.framework.iam.events.publishers.SecurityJwtPublisher;
import io.tech1.framework.iam.repositories.postgres.PostgresUsersSessionsRepository;
import io.tech1.framework.iam.services.postgres.PostgresBaseUsersSessionsService;

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
