package tech1.framework.iam.validators.mongodb;

import tech1.framework.iam.repositories.mongodb.MongoUsersRepository;
import tech1.framework.iam.validators.abtracts.AbstractBaseUsersValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

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
