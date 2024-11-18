package jbst.iam.services;

import jbst.iam.domain.dto.responses.ResponseInvitationCode;
import jbst.iam.domain.dto.responses.ResponseSuperadminSessionsTable;
import jbst.iam.domain.jwt.JwtUser;
import jbst.iam.domain.jwt.RequestAccessToken;
import tech1.framework.foundation.domain.system.reset_server.ResetServerStatus;
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
