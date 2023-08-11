package io.tech1.framework.b2b.base.security.jwt.services;

import io.tech1.framework.b2b.base.security.jwt.domain.dto.responses.ResponseInvitationCode;
import io.tech1.framework.b2b.base.security.jwt.domain.dto.responses.ResponseSuperadminSessionsTable;
import io.tech1.framework.b2b.base.security.jwt.domain.jwt.CookieAccessToken;
import io.tech1.framework.b2b.base.security.jwt.domain.jwt.JwtUser;
import io.tech1.framework.domain.system.reset_server.ResetServerStatus;
import org.springframework.scheduling.annotation.Async;

import java.util.List;

public interface BaseSuperAdminService {
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

    ResponseSuperadminSessionsTable getSessions(CookieAccessToken cookie);
}
