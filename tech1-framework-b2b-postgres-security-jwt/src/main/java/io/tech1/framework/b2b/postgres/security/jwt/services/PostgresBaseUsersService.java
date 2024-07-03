package io.tech1.framework.b2b.postgres.security.jwt.services;

import io.tech1.framework.iam.services.abstracts.AbstractBaseUsersService;
import io.tech1.framework.b2b.postgres.security.jwt.repositories.PostgresUsersRepository;
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
