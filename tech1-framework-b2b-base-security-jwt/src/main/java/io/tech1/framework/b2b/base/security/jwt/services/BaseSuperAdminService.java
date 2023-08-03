package io.tech1.framework.b2b.base.security.jwt.services;

import io.tech1.framework.b2b.base.security.jwt.domain.dto.responses.ResponseInvitationCode;
import io.tech1.framework.b2b.base.security.jwt.domain.dto.responses.ResponseSuperadminSessionsTable;
import io.tech1.framework.b2b.base.security.jwt.domain.jwt.CookieAccessToken;

import java.util.List;

public interface BaseSuperAdminService {
    List<ResponseInvitationCode> findUnused();
    ResponseSuperadminSessionsTable getSessions(CookieAccessToken cookie);
}
