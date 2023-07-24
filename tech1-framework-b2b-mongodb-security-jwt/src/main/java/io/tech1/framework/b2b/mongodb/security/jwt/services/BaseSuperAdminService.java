package io.tech1.framework.b2b.mongodb.security.jwt.services;

import io.tech1.framework.b2b.mongodb.security.jwt.domain.dto.responses.ResponseInvitationCode1;
import io.tech1.framework.b2b.mongodb.security.jwt.domain.dto.responses.ResponseServerSessionsTable;
import io.tech1.framework.b2b.mongodb.security.jwt.domain.jwt.CookieRefreshToken;

import java.util.List;

public interface BaseSuperAdminService {
    List<ResponseInvitationCode1> findUnused();
    ResponseServerSessionsTable getServerSessions(CookieRefreshToken cookie);
}
