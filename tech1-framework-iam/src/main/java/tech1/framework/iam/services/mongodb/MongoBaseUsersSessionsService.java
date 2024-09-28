package tech1.framework.iam.services.mongodb;

import tech1.framework.foundation.utils.UserMetadataUtils;
import tech1.framework.iam.events.publishers.SecurityJwtPublisher;
import tech1.framework.iam.repositories.mongodb.MongoUsersSessionsRepository;
import tech1.framework.iam.services.abstracts.AbstractBaseUsersSessionsService;
import tech1.framework.iam.utils.SecurityJwtTokenUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class MongoBaseUsersSessionsService extends AbstractBaseUsersSessionsService {

    @Autowired
    public MongoBaseUsersSessionsService(
            SecurityJwtPublisher securityJwtPublisher,
            MongoUsersSessionsRepository usersSessionsRepository,
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
