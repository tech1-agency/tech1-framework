package io.tech1.framework.iam.services.mongodb;

import io.tech1.framework.iam.assistants.userdetails.MongoUserDetailsAssistant;
import io.tech1.framework.iam.repositories.mongodb.MongoUsersSessionsRepository;
import io.tech1.framework.iam.services.abstracts.AbstractTokensContextThrowerService;
import io.tech1.framework.iam.utils.SecurityJwtTokenUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class MongoTokensContextThrowerService extends AbstractTokensContextThrowerService {

    @Autowired
    public MongoTokensContextThrowerService(
            MongoUserDetailsAssistant userDetailsAssistant,
            MongoUsersSessionsRepository usersSessionsRepository,
            SecurityJwtTokenUtils securityJwtTokenUtils
    ) {
        super(
                userDetailsAssistant,
                usersSessionsRepository,
                securityJwtTokenUtils
        );
    }
}
