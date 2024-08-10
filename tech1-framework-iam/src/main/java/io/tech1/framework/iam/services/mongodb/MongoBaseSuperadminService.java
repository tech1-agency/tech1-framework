package io.tech1.framework.iam.services.mongodb;

import io.tech1.framework.foundation.incidents.events.publishers.IncidentPublisher;
import io.tech1.framework.iam.repositories.mongodb.MongoInvitationCodesRepository;
import io.tech1.framework.iam.repositories.mongodb.MongoUsersSessionsRepository;
import io.tech1.framework.iam.services.abstracts.AbstractBaseSuperadminService;
import io.tech1.framework.iam.sessions.SessionRegistry;
import io.tech1.framework.iam.tasks.superadmin.AbstractSuperAdminResetServerTask;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class MongoBaseSuperadminService extends AbstractBaseSuperadminService {

    @Autowired
    public MongoBaseSuperadminService(
            IncidentPublisher incidentPublisher,
            SessionRegistry sessionRegistry,
            MongoInvitationCodesRepository invitationCodesRepository,
            MongoUsersSessionsRepository usersSessionsRepository,
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
