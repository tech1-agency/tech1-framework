package io.tech1.framework.b2b.postgres.security.jwt.assistants.userdetails;

import io.tech1.framework.b2b.base.security.jwt.assistants.userdetails.AbstractJwtUserDetailsService;
import io.tech1.framework.b2b.postgres.security.jwt.repositories.PostgresUsersRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@SuppressWarnings("SpringJavaInjectionPointsAutowiringInspection")
@Slf4j
@Service
public class PostgresUserDetailsAssistant extends AbstractJwtUserDetailsService {

    @Autowired
    public PostgresUserDetailsAssistant(
            PostgresUsersRepository usersRepository
    ) {
        super(
                usersRepository
        );
    }
}
