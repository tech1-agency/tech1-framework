package io.tech1.framework.b2b.postgres.security.jwt.services;

import io.tech1.framework.b2b.base.security.jwt.services.abstracts.AbstractBaseSuperAdminService;
import io.tech1.framework.b2b.base.security.jwt.sessions.SessionRegistry;
import io.tech1.framework.b2b.postgres.security.jwt.repositories.PostgresInvitationCodesRepository;
import io.tech1.framework.b2b.postgres.security.jwt.repositories.PostgresUsersSessionsRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class PostgresBaseSuperAdminService extends AbstractBaseSuperAdminService {

    @Autowired
    public PostgresBaseSuperAdminService(
            SessionRegistry sessionRegistry,
            PostgresInvitationCodesRepository invitationCodesRepository,
            PostgresUsersSessionsRepository usersSessionsRepository
    ) {
        super(
                sessionRegistry,
                invitationCodesRepository,
                usersSessionsRepository
        );
    }
}
