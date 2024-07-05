package io.tech1.framework.iam.assistants.userdetails;

import io.tech1.framework.iam.repositories.postgres.PostgresUsersRepository;

public class PostgresUserDetailsAssistant extends AbstractJwtUserDetailsService {

    public PostgresUserDetailsAssistant(
            PostgresUsersRepository usersRepository
    ) {
        super(
                usersRepository
        );
    }
}
