package tech1.framework.iam.services.postgres;

import tech1.framework.iam.events.publishers.SecurityJwtPublisher;
import tech1.framework.iam.services.abstracts.AbstractBaseUsersSessionsService;
import tech1.framework.iam.utils.SecurityJwtTokenUtils;
import tech1.framework.iam.repositories.postgres.PostgresUsersSessionsRepository;
import tech1.framework.foundation.utils.UserMetadataUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class PostgresBaseUsersSessionsService extends AbstractBaseUsersSessionsService {

    @Autowired
    public PostgresBaseUsersSessionsService(
            SecurityJwtPublisher securityJwtPublisher,
            PostgresUsersSessionsRepository usersSessionsRepository,
            UserMetadataUtils userMetadataUtils,
            SecurityJwtTokenUtils securityJwtTokenUtils
    ) {
        super(
                securityJwtPublisher,
                usersSessionsRepository,
                userMetadataUtils,
                securityJwtTokenUtils
        );
    }
}
