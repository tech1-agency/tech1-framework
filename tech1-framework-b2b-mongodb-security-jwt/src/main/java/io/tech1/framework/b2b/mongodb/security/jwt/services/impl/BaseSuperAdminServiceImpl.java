package io.tech1.framework.b2b.mongodb.security.jwt.services.impl;

import io.tech1.framework.b2b.mongodb.security.jwt.domain.dto.responses.ResponseInvitationCode1;
import io.tech1.framework.b2b.mongodb.security.jwt.domain.dto.responses.ResponseServerSessionsTable;
import io.tech1.framework.b2b.mongodb.security.jwt.domain.dto.responses.ResponseUserSession3;
import io.tech1.framework.b2b.mongodb.security.jwt.repositories.InvitationCodeRepository;
import io.tech1.framework.b2b.mongodb.security.jwt.repositories.UserSessionRepository;
import io.tech1.framework.b2b.mongodb.security.jwt.services.BaseSuperAdminService;
import io.tech1.framework.b2b.mongodb.security.jwt.sessions.SessionRegistry;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static io.tech1.framework.b2b.mongodb.security.jwt.comparators.SecurityJwtComparators.INVITATION_CODE_1;

@Slf4j
@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class BaseSuperAdminServiceImpl implements BaseSuperAdminService {

    // Sessions
    private final SessionRegistry sessionRegistry;
    // Repositories
    private final InvitationCodeRepository invitationCodeRepository;
    private final UserSessionRepository userSessionRepository;

    @Override
    public List<ResponseInvitationCode1> findUnused() {
        var invitationCodes = this.invitationCodeRepository.findByInvitedIsNull();
        return invitationCodes.stream()
                .map(ResponseInvitationCode1::new)
                .sorted(INVITATION_CODE_1)
                .collect(Collectors.toList());
    }

    @Override
    public ResponseServerSessionsTable getServerSessions() {
        var dbUserSessions = this.userSessionRepository.findAll();
        var activeSessionsRefreshTokens = this.sessionRegistry.getActiveSessionsRefreshTokens();
        List<ResponseUserSession3> activeSessions = new ArrayList<>();
        List<ResponseUserSession3> inactiveSessions = new ArrayList<>();
        dbUserSessions.forEach(dbUserSession -> {
            var session = new ResponseUserSession3(dbUserSession);
            if (activeSessionsRefreshTokens.contains(dbUserSession.getJwtRefreshToken())) {
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
