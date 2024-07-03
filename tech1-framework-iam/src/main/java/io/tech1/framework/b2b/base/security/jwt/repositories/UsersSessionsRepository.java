package io.tech1.framework.b2b.base.security.jwt.repositories;

import io.tech1.framework.b2b.base.security.jwt.domain.db.UserSession;
import io.tech1.framework.b2b.base.security.jwt.domain.dto.responses.ResponseSuperadminSessionsTable;
import io.tech1.framework.b2b.base.security.jwt.domain.dto.responses.ResponseUserSession2;
import io.tech1.framework.b2b.base.security.jwt.domain.identifiers.UserSessionId;
import io.tech1.framework.b2b.base.security.jwt.domain.jwt.RequestAccessToken;
import io.tech1.framework.b2b.base.security.jwt.domain.jwt.JwtAccessToken;
import io.tech1.framework.b2b.base.security.jwt.domain.jwt.JwtRefreshToken;
import io.tech1.framework.foundation.domain.base.Username;
import io.tech1.framework.foundation.domain.tuples.TuplePresence;

import java.util.List;
import java.util.Set;

public interface UsersSessionsRepository {
    TuplePresence<UserSession> isPresent(UserSessionId userSessionId, Username username);
    TuplePresence<UserSession> isPresent(UserSessionId userSessionId);
    TuplePresence<UserSession> isPresent(JwtAccessToken accessToken);
    TuplePresence<UserSession> isPresent(JwtRefreshToken refreshToken);
    List<ResponseUserSession2> getUsersSessionsTable(Username username, RequestAccessToken requestAccessToken);
    ResponseSuperadminSessionsTable getSessionsTable(Set<JwtAccessToken> activeAccessTokens, RequestAccessToken requestAccessToken);
    List<UserSession> findByUsernameInAsAny(Set<Username> usernames);
    void enableMetadataRenewCron();
    UserSession enableMetadataRenewManually(UserSessionId sessionId);
    void delete(UserSessionId sessionId);
    long delete(Set<UserSessionId> sessionsIds);
    void deleteByUsernameExceptAccessToken(Username username, RequestAccessToken requestAccessToken);
    void deleteExceptAccessToken(RequestAccessToken requestAccessToken);
    UserSession saveAs(UserSession userSession);
}
