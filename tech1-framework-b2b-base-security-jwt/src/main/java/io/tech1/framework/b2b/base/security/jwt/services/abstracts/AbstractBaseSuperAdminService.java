package io.tech1.framework.b2b.base.security.jwt.services.abstracts;

import io.tech1.framework.b2b.base.security.jwt.domain.dto.responses.ResponseInvitationCode;
import io.tech1.framework.b2b.base.security.jwt.domain.dto.responses.ResponseSuperadminSessionsTable;
import io.tech1.framework.b2b.base.security.jwt.domain.jwt.RequestAccessToken;
import io.tech1.framework.b2b.base.security.jwt.domain.jwt.JwtUser;
import io.tech1.framework.b2b.base.security.jwt.repositories.InvitationCodesRepository;
import io.tech1.framework.b2b.base.security.jwt.repositories.UsersSessionsRepository;
import io.tech1.framework.b2b.base.security.jwt.services.BaseSuperAdminService;
import io.tech1.framework.b2b.base.security.jwt.sessions.SessionRegistry;
import io.tech1.framework.b2b.base.security.jwt.tasks.AbstractSuperAdminResetServerTask;
import io.tech1.framework.domain.system.reset_server.ResetServerStatus;
import io.tech1.framework.incidents.domain.system.IncidentSystemResetServerCompleted;
import io.tech1.framework.incidents.domain.system.IncidentSystemResetServerStarted;
import io.tech1.framework.incidents.events.publishers.IncidentPublisher;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;

import java.util.List;

@AllArgsConstructor(access = AccessLevel.PROTECTED)
public abstract class AbstractBaseSuperAdminService implements BaseSuperAdminService {

    // Incidents
    protected final IncidentPublisher incidentPublisher;
    // Sessions
    protected final SessionRegistry sessionRegistry;
    // Repositories
    protected final InvitationCodesRepository invitationCodesRepository;
    protected final UsersSessionsRepository usersSessionsRepository;
    // Tasks
    protected final AbstractSuperAdminResetServerTask resetServerTask;

    // =================================================================================================================
    // Server
    // =================================================================================================================
    @Override
    public ResetServerStatus getResetServerStatus() {
        return this.resetServerTask.getStatus();
    }

    @Override
    public void resetServerBy(JwtUser user) {
        this.incidentPublisher.publishResetServerStarted(new IncidentSystemResetServerStarted(user.username()));

        this.resetServerTask.reset(user);

        this.incidentPublisher.publishResetServerCompleted(new IncidentSystemResetServerCompleted(user.username()));
    }

    // =================================================================================================================
    // Invitation Codes
    // =================================================================================================================
    @Override
    public List<ResponseInvitationCode> findUnused() {
        return this.invitationCodesRepository.findUnused();
    }

    // =================================================================================================================
    // Users Sessions
    // =================================================================================================================
    @Override
    public ResponseSuperadminSessionsTable getSessions(RequestAccessToken requestAccessToken) {
        var activeAccessTokens = this.sessionRegistry.getActiveSessionsAccessTokens();
        return this.usersSessionsRepository.getSessionsTable(activeAccessTokens, requestAccessToken);
    }
}
