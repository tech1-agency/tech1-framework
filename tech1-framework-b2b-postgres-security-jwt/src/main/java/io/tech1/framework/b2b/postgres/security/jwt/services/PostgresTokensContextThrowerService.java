package io.tech1.framework.b2b.postgres.security.jwt.services;

import io.tech1.framework.b2b.base.security.jwt.assistants.userdetails.JwtUserDetailsService;
import io.tech1.framework.b2b.base.security.jwt.services.abstracts.AbstractTokensContextThrowerService;
import io.tech1.framework.b2b.base.security.jwt.utils.SecurityJwtTokenUtils;
import io.tech1.framework.b2b.postgres.security.jwt.repositories.PostgresUsersSessionsRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class PostgresTokensContextThrowerService extends AbstractTokensContextThrowerService {

    @Autowired
    public PostgresTokensContextThrowerService(
            JwtUserDetailsService jwtUserDetailsService,
            PostgresUsersSessionsRepository usersSessionsRepository,
            SecurityJwtTokenUtils securityJwtTokenUtils
    ) {
        super(
                jwtUserDetailsService,
                usersSessionsRepository,
                securityJwtTokenUtils
        );
    }
}
