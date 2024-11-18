package jbst.iam.assistants.userdetails;

import jbst.iam.repositories.mongodb.MongoUsersRepository;

public class MongoUserDetailsAssistant extends AbstractJwtUserDetailsService {

    public MongoUserDetailsAssistant(
            MongoUsersRepository usersRepository
    ) {
        super(
                usersRepository
        );
    }
}
