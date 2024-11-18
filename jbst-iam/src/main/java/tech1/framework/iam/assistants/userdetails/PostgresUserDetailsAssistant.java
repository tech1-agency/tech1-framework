package tech1.framework.iam.assistants.userdetails;

import tech1.framework.iam.repositories.postgres.PostgresUsersRepository;

public class PostgresUserDetailsAssistant extends AbstractJwtUserDetailsService {

    public PostgresUserDetailsAssistant(
            PostgresUsersRepository usersRepository
    ) {
        super(
                usersRepository
        );
    }
}
