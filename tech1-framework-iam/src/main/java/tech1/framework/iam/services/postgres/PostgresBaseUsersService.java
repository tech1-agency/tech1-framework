package tech1.framework.iam.services.postgres;

import tech1.framework.iam.services.abstracts.AbstractBaseUsersService;
import tech1.framework.iam.repositories.postgres.PostgresUsersRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class PostgresBaseUsersService extends AbstractBaseUsersService {

    @Autowired
    public PostgresBaseUsersService(
            PostgresUsersRepository usersRepository,
            BCryptPasswordEncoder bCryptPasswordEncoder
    ) {
        super(
                usersRepository,
                bCryptPasswordEncoder
        );
    }
}
