package jbst.iam.validators.mongodb;

import jbst.iam.repositories.mongodb.MongoUsersRepository;
import jbst.iam.validators.abtracts.AbstractBaseUsersValidator;
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
