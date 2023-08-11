package io.tech1.framework.b2b.mongodb.security.jwt.validators;

import io.tech1.framework.b2b.base.security.jwt.validators.abtracts.AbstractBaseUsersSessionsRequestsValidator;
import io.tech1.framework.b2b.mongodb.security.jwt.repositories.MongoUsersSessionsRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@SuppressWarnings("SpringJavaInjectionPointsAutowiringInspection")
@Slf4j
@Component
public class MongoBaseUsersSessionsRequestsValidator extends AbstractBaseUsersSessionsRequestsValidator {

    @Autowired
    public MongoBaseUsersSessionsRequestsValidator(
            MongoUsersSessionsRepository usersSessionsRepository
    ) {
        super(
                usersSessionsRepository
        );
    }
}
