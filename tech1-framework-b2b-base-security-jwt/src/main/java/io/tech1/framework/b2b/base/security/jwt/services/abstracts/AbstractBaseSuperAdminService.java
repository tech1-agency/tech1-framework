package io.tech1.framework.b2b.base.security.jwt.services.abstracts;

import io.tech1.framework.b2b.base.security.jwt.domain.dto.responses.ResponseInvitationCode;
import io.tech1.framework.b2b.base.security.jwt.domain.dto.responses.ResponseServerSessionsTable;
import io.tech1.framework.b2b.base.security.jwt.domain.jwt.CookieAccessToken;
import io.tech1.framework.b2b.base.security.jwt.repositories.AnyDbInvitationCodesRepository;
import io.tech1.framework.b2b.base.security.jwt.repositories.AnyDbUsersSessionsRepository;
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
    protected final AnyDbInvitationCodesRepository anyDbInvitationCodesRepository;
    protected final AnyDbUsersSessionsRepository anyDbUsersSessionsRepository;

    @Override
    public List<ResponseInvitationCode> findUnused() {
        return this.anyDbInvitationCodesRepository.findUnused();
    }

    @Override
    public ResponseServerSessionsTable getServerSessions(CookieAccessToken cookie) {
        var activeAccessTokens = this.sessionRegistry.getActiveSessionsAccessTokens();
        return this.anyDbUsersSessionsRepository.findAllByCookieAsSession2(activeAccessTokens, cookie);
    }
}
