package io.tech1.framework.b2b.postgres.security.jwt.validators;

import io.tech1.framework.b2b.base.security.jwt.validators.abtracts.AbstractBaseUsersSessionsRequestsValidator;
import io.tech1.framework.b2b.postgres.security.jwt.repositories.PostgresUsersSessionsRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class PostgresBaseUsersSessionsRequestsValidator  extends AbstractBaseUsersSessionsRequestsValidator {

    @Autowired
    public PostgresBaseUsersSessionsRequestsValidator(
            PostgresUsersSessionsRepository usersSessionsRepository
    ) {
        super(
                usersSessionsRepository
        );
    }
}
