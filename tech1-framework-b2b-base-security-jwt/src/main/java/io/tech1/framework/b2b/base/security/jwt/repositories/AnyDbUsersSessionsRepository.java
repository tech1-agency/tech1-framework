package io.tech1.framework.b2b.base.security.jwt.repositories;

import io.tech1.framework.b2b.base.security.jwt.domain.db.AnyDbUserSession;
import io.tech1.framework.b2b.base.security.jwt.domain.dto.responses.ResponseSuperadminSessionsTable;
import io.tech1.framework.b2b.base.security.jwt.domain.dto.responses.ResponseUserSession2;
import io.tech1.framework.b2b.base.security.jwt.domain.identifiers.UserSessionId;
import io.tech1.framework.b2b.base.security.jwt.domain.jwt.CookieAccessToken;
import io.tech1.framework.b2b.base.security.jwt.domain.jwt.JwtAccessToken;
import io.tech1.framework.b2b.base.security.jwt.domain.jwt.JwtRefreshToken;
import io.tech1.framework.domain.base.Username;
import io.tech1.framework.domain.tuples.TuplePresence;

import java.util.List;
import java.util.Set;

// TODO [YY] add unit tests
public interface AnyDbUsersSessionsRepository {
    TuplePresence<AnyDbUserSession> isPresent(UserSessionId userSessionId);
    TuplePresence<AnyDbUserSession> isPresent(JwtAccessToken accessToken);
    TuplePresence<AnyDbUserSession> isPresent(JwtRefreshToken refreshToken);
    List<ResponseUserSession2> getUsersSessionsTable(Username username, CookieAccessToken cookie);
    ResponseSuperadminSessionsTable getSessionsTable(Set<JwtAccessToken> activeAccessTokens, CookieAccessToken cookie);
    List<AnyDbUserSession> findByUsernameInAsAny(Set<Username> usernames);
    void delete(UserSessionId sessionId);
    long deleteByUsersSessionsIds(List<UserSessionId> sessionsIds);
    void deleteByUsernameExceptAccessToken(Username username, CookieAccessToken cookie);
    void deleteExceptAccessToken(CookieAccessToken cookie);
    UserSessionId saveAs(AnyDbUserSession userSession);
}
