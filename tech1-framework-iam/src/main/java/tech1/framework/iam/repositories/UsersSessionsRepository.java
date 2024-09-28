package tech1.framework.iam.repositories;

import tech1.framework.iam.domain.db.UserSession;
import tech1.framework.iam.domain.dto.responses.ResponseSuperadminSessionsTable;
import tech1.framework.iam.domain.dto.responses.ResponseUserSession2;
import tech1.framework.iam.domain.identifiers.UserSessionId;
import tech1.framework.iam.domain.jwt.JwtAccessToken;
import tech1.framework.iam.domain.jwt.JwtRefreshToken;
import tech1.framework.iam.domain.jwt.RequestAccessToken;
import tech1.framework.foundation.domain.base.Username;
import tech1.framework.foundation.domain.tuples.TuplePresence;

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
