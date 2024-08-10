package io.tech1.framework.iam.services.abstracts;

import io.tech1.framework.iam.domain.dto.responses.ResponseInvitationCode;
import io.tech1.framework.iam.domain.dto.responses.ResponseSuperadminSessionsTable;
import io.tech1.framework.iam.domain.jwt.JwtUser;
import io.tech1.framework.iam.domain.jwt.RequestAccessToken;
import io.tech1.framework.iam.repositories.InvitationCodesRepository;
import io.tech1.framework.iam.repositories.UsersSessionsRepository;
import io.tech1.framework.iam.services.BaseSuperadminService;
import io.tech1.framework.iam.sessions.SessionRegistry;
import io.tech1.framework.iam.tasks.superadmin.AbstractSuperAdminResetServerTask;
import io.tech1.framework.foundation.domain.system.reset_server.ResetServerStatus;
import io.tech1.framework.foundation.incidents.domain.system.IncidentSystemResetServerCompleted;
import io.tech1.framework.foundation.incidents.domain.system.IncidentSystemResetServerStarted;
import io.tech1.framework.foundation.incidents.events.publishers.IncidentPublisher;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;

import java.util.List;

@AllArgsConstructor(access = AccessLevel.PROTECTED)
public abstract class AbstractBaseSuperadminService implements BaseSuperadminService {

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
