package io.tech1.framework.b2b.base.security.jwt.services;

import io.tech1.framework.b2b.base.security.jwt.domain.dto.responses.ResponseInvitationCode;
import io.tech1.framework.b2b.base.security.jwt.domain.dto.responses.ResponseServerSessionsTable;
import io.tech1.framework.b2b.base.security.jwt.domain.jwt.CookieRefreshToken;

import java.util.List;

public interface BaseSuperAdminService {
    List<ResponseInvitationCode> findUnused();
    ResponseServerSessionsTable getServerSessions(CookieRefreshToken cookie);
}
