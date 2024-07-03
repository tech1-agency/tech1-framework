package io.tech1.framework.b2b.mongodb.security.jwt.validators;

import io.tech1.framework.iam.validators.abtracts.AbstractBaseUsersValidator;
import io.tech1.framework.b2b.mongodb.security.jwt.repositories.MongoUsersRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@SuppressWarnings("SpringJavaInjectionPointsAutowiringInspection")
@Slf4j
@Component
public class MongoBaseUsersValidator extends AbstractBaseUsersValidator {

    @Autowired
    public MongoBaseUsersValidator(
            MongoUsersRepository usersRepository
    ) {
        super(
                usersRepository
        );
    }

}
