package io.tech1.framework.iam.assistants.userdetails;

import io.tech1.framework.iam.repositories.mongo.MongoUsersRepository;

public class MongoUserDetailsAssistant extends AbstractJwtUserDetailsService {

    public MongoUserDetailsAssistant(
            MongoUsersRepository usersRepository
    ) {
        super(
                usersRepository
        );
    }
}
