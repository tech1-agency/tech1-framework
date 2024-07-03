package io.tech1.framework.iam.services;

import io.tech1.framework.iam.domain.dto.responses.ResponseInvitationCode;
import io.tech1.framework.iam.domain.dto.responses.ResponseSuperadminSessionsTable;
import io.tech1.framework.iam.domain.jwt.JwtUser;
import io.tech1.framework.iam.domain.jwt.RequestAccessToken;
import io.tech1.framework.foundation.domain.system.reset_server.ResetServerStatus;
import org.springframework.scheduling.annotation.Async;

import java.util.List;

public interface BaseSuperadminService {
    // =================================================================================================================
    // Server
    // =================================================================================================================
    ResetServerStatus getResetServerStatus();
    @Async
    void resetServerBy(JwtUser user);

    // =================================================================================================================
    // Invitation Codes
    // =================================================================================================================

    List<ResponseInvitationCode> findUnused();

    // =================================================================================================================
    // Users Sessions
    // =================================================================================================================

    ResponseSuperadminSessionsTable getSessions(RequestAccessToken requestAccessToken);
}
