package jbst.iam.services.postgres;

import jbst.iam.repositories.postgres.PostgresInvitationCodesRepository;
import jbst.iam.repositories.postgres.PostgresUsersSessionsRepository;
import jbst.iam.services.abstracts.AbstractBaseSuperadminService;
import jbst.iam.sessions.SessionRegistry;
import jbst.iam.tasks.superadmin.AbstractSuperAdminResetServerTask;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import jbst.foundation.incidents.events.publishers.IncidentPublisher;

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
