package jbst.iam.validators.mongodb;

import jbst.iam.repositories.mongodb.MongoUsersTokensRepository;
import jbst.iam.validators.abtracts.AbstractBaseUsersTokensRequestsValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class MongoBaseUsersTokensRequestsValidator extends AbstractBaseUsersTokensRequestsValidator {

    @Autowired
    public MongoBaseUsersTokensRequestsValidator(
            MongoUsersTokensRepository usersTokensRepository
    ) {
        super(
                usersTokensRepository
        );
    }
}
