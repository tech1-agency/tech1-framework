package jbst.iam.services.postgres;

import jbst.iam.repositories.postgres.PostgresUsersRepository;
import jbst.iam.repositories.postgres.PostgresUsersTokensRepository;
import jbst.iam.services.abstracts.AbstractBaseUsersTokensService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class PostgresBaseUsersTokensService extends AbstractBaseUsersTokensService {

    @Autowired
    public PostgresBaseUsersTokensService(
            PostgresUsersTokensRepository usersTokensRepository,
            PostgresUsersRepository usersRepository
    ) {
        super(
                usersTokensRepository,
                usersRepository
        );
    }
}
