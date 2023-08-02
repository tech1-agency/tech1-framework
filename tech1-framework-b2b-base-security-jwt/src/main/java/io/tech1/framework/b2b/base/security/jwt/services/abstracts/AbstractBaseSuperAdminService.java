package io.tech1.framework.b2b.base.security.jwt.services.abstracts;

import io.tech1.framework.b2b.base.security.jwt.domain.dto.responses.ResponseInvitationCode;
import io.tech1.framework.b2b.base.security.jwt.domain.dto.responses.ResponseServerSessionsTable;
import io.tech1.framework.b2b.base.security.jwt.domain.dto.responses.ResponseUserSession2;
import io.tech1.framework.b2b.base.security.jwt.domain.jwt.CookieAccessToken;
import io.tech1.framework.b2b.base.security.jwt.repositories.AnyDbInvitationCodesRepository;
import io.tech1.framework.b2b.base.security.jwt.repositories.AnyDbUsersSessionsRepository;
import io.tech1.framework.b2b.base.security.jwt.services.BaseSuperAdminService;
import io.tech1.framework.b2b.base.security.jwt.sessions.SessionRegistry;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;

import java.util.ArrayList;
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
        // TODO [YY] add sorting
        var sessionsTuples2 = this.anyDbUsersSessionsRepository.findAllByCookieAsSession2(cookie);
        var activeSessionsAccessTokens = this.sessionRegistry.getActiveSessionsAccessTokens();
        List<ResponseUserSession2> activeSessions = new ArrayList<>();
        List<ResponseUserSession2> inactiveSessions = new ArrayList<>();
        sessionsTuples2.forEach(sessionTuple -> {
            var session = sessionTuple.a();
            var accessToken = sessionTuple.b();
            if (activeSessionsAccessTokens.contains(accessToken)) {
                activeSessions.add(session);
            } else {
                inactiveSessions.add(session);
            }
        });
        return ResponseServerSessionsTable.of(
                activeSessions,
                inactiveSessions
        );
    }
}
