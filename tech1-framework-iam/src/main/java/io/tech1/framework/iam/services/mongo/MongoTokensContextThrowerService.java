package io.tech1.framework.iam.services.mongo;

import io.tech1.framework.iam.services.abstracts.AbstractTokensContextThrowerService;
import io.tech1.framework.iam.utils.SecurityJwtTokenUtils;
import io.tech1.framework.iam.assistants.userdetails.MongoUserDetailsAssistant;
import io.tech1.framework.iam.repositories.mongo.MongoUsersSessionsRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@SuppressWarnings("SpringJavaInjectionPointsAutowiringInspection")
@Slf4j
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
