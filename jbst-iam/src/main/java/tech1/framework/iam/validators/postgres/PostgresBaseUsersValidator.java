package tech1.framework.iam.validators.postgres;

import tech1.framework.iam.validators.abtracts.AbstractBaseUsersValidator;
import tech1.framework.iam.repositories.postgres.PostgresUsersRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class PostgresBaseUsersValidator extends AbstractBaseUsersValidator {

    @Autowired
    public PostgresBaseUsersValidator(
            PostgresUsersRepository usersRepository
    ) {
        super(
                usersRepository
        );
    }

}
