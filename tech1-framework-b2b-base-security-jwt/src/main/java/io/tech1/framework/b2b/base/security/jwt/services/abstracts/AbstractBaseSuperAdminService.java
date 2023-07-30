package io.tech1.framework.b2b.base.security.jwt.services.abstracts;

import io.tech1.framework.b2b.base.security.jwt.domain.dto.responses.ResponseInvitationCode;
import io.tech1.framework.b2b.base.security.jwt.domain.dto.responses.ResponseServerSessionsTable;
import io.tech1.framework.b2b.base.security.jwt.domain.dto.responses.ResponseUserSession2;
import io.tech1.framework.b2b.base.security.jwt.domain.jwt.CookieRefreshToken;
import io.tech1.framework.b2b.base.security.jwt.repositories.AnyDbInvitationCodesRepository;
import io.tech1.framework.b2b.base.security.jwt.repositories.AnyDbUsersSessionsRepository;
import io.tech1.framework.b2b.base.security.jwt.services.BaseSuperAdminService;
import io.tech1.framework.b2b.base.security.jwt.sessions.SessionRegistry;

import java.util.ArrayList;
import java.util.List;

import static io.tech1.framework.b2b.base.security.jwt.comparators.SecurityJwtComparators.INVITATION_CODE_1;

public abstract class AbstractBaseSuperAdminService implements BaseSuperAdminService {

    // Sessions
    private final SessionRegistry sessionRegistry;
    // Repositories
    private final AnyDbInvitationCodesRepository anyDbInvitationCodesRepository;
    private final AnyDbUsersSessionsRepository anyDbUsersSessionsRepository;

    protected AbstractBaseSuperAdminService(
            SessionRegistry sessionRegistry,
            AnyDbInvitationCodesRepository anyDbInvitationCodesRepository,
            AnyDbUsersSessionsRepository anyDbUsersSessionsRepository
    ) {
        this.sessionRegistry = sessionRegistry;
        this.anyDbInvitationCodesRepository = anyDbInvitationCodesRepository;
        this.anyDbUsersSessionsRepository = anyDbUsersSessionsRepository;
    }

    @Override
    public List<ResponseInvitationCode> findUnused() {
        var invitationCodes = this.anyDbInvitationCodesRepository.findUnused();
        // WARNING: consider migrate sorting -> database
        invitationCodes.sort(INVITATION_CODE_1);
        return invitationCodes;
    }

    @Override
    public ResponseServerSessionsTable getServerSessions(CookieRefreshToken cookie) {
        var sessionsTuples2 = this.anyDbUsersSessionsRepository.findAllByCookieAsSession2(cookie);
        var activeSessionsRefreshTokens = this.sessionRegistry.getActiveSessionsRefreshTokens();
        List<ResponseUserSession2> activeSessions = new ArrayList<>();
        List<ResponseUserSession2> inactiveSessions = new ArrayList<>();
        sessionsTuples2.forEach(sessionTuple -> {
            var session = sessionTuple.a();
            var jwtRefreshToken = sessionTuple.b();
            if (activeSessionsRefreshTokens.contains(jwtRefreshToken)) {
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
