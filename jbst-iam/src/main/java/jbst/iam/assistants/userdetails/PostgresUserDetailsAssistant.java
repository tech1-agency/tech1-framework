package jbst.iam.assistants.userdetails;

import jbst.iam.repositories.postgres.PostgresUsersRepository;

public class PostgresUserDetailsAssistant extends AbstractJwtUserDetailsService {

    public PostgresUserDetailsAssistant(
            PostgresUsersRepository usersRepository
    ) {
        super(
                usersRepository
        );
    }
}
