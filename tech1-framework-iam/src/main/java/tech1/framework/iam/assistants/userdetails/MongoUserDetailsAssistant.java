package tech1.framework.iam.assistants.userdetails;

import tech1.framework.iam.repositories.mongodb.MongoUsersRepository;

public class MongoUserDetailsAssistant extends AbstractJwtUserDetailsService {

    public MongoUserDetailsAssistant(
            MongoUsersRepository usersRepository
    ) {
        super(
                usersRepository
        );
    }
}
