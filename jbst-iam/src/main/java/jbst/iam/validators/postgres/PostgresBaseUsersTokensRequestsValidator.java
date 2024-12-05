package jbst.iam.validators.postgres;

import jbst.iam.repositories.postgres.PostgresUsersTokensRepository;
import jbst.iam.validators.abtracts.AbstractBaseUsersTokensRequestsValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class PostgresBaseUsersTokensRequestsValidator extends AbstractBaseUsersTokensRequestsValidator {

    @Autowired
    public PostgresBaseUsersTokensRequestsValidator(
            PostgresUsersTokensRepository usersTokensRepository
    ) {
        super(
                usersTokensRepository
        );
    }
}
