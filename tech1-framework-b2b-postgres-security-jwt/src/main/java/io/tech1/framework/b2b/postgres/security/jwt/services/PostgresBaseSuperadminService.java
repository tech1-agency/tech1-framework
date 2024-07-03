package io.tech1.framework.b2b.postgres.security.jwt.services;

import io.tech1.framework.b2b.base.security.jwt.services.abstracts.AbstractBaseSuperadminService;
import io.tech1.framework.b2b.base.security.jwt.sessions.SessionRegistry;
import io.tech1.framework.b2b.base.security.jwt.tasks.AbstractSuperAdminResetServerTask;
import io.tech1.framework.b2b.postgres.security.jwt.repositories.PostgresInvitationCodesRepository;
import io.tech1.framework.b2b.postgres.security.jwt.repositories.PostgresUsersSessionsRepository;
import io.tech1.framework.foundation.incidents.events.publishers.IncidentPublisher;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@SuppressWarnings("SpringJavaInjectionPointsAutowiringInspection")
@Slf4j
@Service
public class PostgresBaseSuperadminService extends AbstractBaseSuperadminService {

    @Autowired
    public PostgresBaseSuperadminService(
            IncidentPublisher incidentPublisher,
            SessionRegistry sessionRegistry,
            PostgresInvitationCodesRepository invitationCodesRepository,
            PostgresUsersSessionsRepository usersSessionsRepository,
            AbstractSuperAdminResetServerTask abstractSuperAdminResetServerTask
    ) {
        super(
                incidentPublisher,
                sessionRegistry,
                invitationCodesRepository,
                usersSessionsRepository,
                abstractSuperAdminResetServerTask
        );
    }
}
