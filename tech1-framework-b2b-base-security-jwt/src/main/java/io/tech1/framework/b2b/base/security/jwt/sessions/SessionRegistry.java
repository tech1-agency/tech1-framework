package io.tech1.framework.b2b.base.security.jwt.sessions;

import io.tech1.framework.b2b.base.security.jwt.domain.jwt.JwtRefreshToken;
import io.tech1.framework.b2b.base.security.jwt.domain.sessions.Session;
import io.tech1.framework.domain.base.Username;
import org.springframework.scheduling.annotation.Async;

import java.util.Set;

public interface SessionRegistry {
    Set<String> getActiveSessionsUsernamesIdentifiers();
    Set<Username> getActiveSessionsUsernames();
    Set<JwtRefreshToken> getActiveSessionsRefreshTokens();

    @Async
    void register(Session session);
    @Async
    void renew(Session oldSession, Session newSession);
    @Async
    void logout(Session session);

    void cleanByExpiredRefreshTokens(Set<Username> usernames);
}
