package io.tech1.framework.b2b.base.security.jwt.services.abstracts;

import io.tech1.framework.b2b.base.security.jwt.domain.dto.responses.ResponseInvitationCode;
import io.tech1.framework.b2b.base.security.jwt.domain.dto.responses.ResponseSuperadminSessionsTable;
import io.tech1.framework.b2b.base.security.jwt.domain.jwt.CookieAccessToken;
import io.tech1.framework.b2b.base.security.jwt.repositories.InvitationCodesRepository;
import io.tech1.framework.b2b.base.security.jwt.repositories.UsersSessionsRepository;
import io.tech1.framework.b2b.base.security.jwt.services.BaseSuperAdminService;
import io.tech1.framework.b2b.base.security.jwt.sessions.SessionRegistry;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;

import java.util.List;

@AllArgsConstructor(access = AccessLevel.PROTECTED)
public abstract class AbstractBaseSuperAdminService implements BaseSuperAdminService {

    // Sessions
    protected final SessionRegistry sessionRegistry;
    // Repositories
    protected final InvitationCodesRepository invitationCodesRepository;
    protected final UsersSessionsRepository usersSessionsRepository;

    @Override
    public List<ResponseInvitationCode> findUnused() {
        return this.invitationCodesRepository.findUnused();
    }

    @Override
    public ResponseSuperadminSessionsTable getSessions(CookieAccessToken cookie) {
        var activeAccessTokens = this.sessionRegistry.getActiveSessionsAccessTokens();
        return this.usersSessionsRepository.getSessionsTable(activeAccessTokens, cookie);
    }
}
