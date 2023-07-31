package io.tech1.framework.b2b.base.security.jwt.repositories;

import io.tech1.framework.b2b.base.security.jwt.domain.db.AnyDbUserSession;
import io.tech1.framework.b2b.base.security.jwt.domain.dto.responses.ResponseUserSession2;
import io.tech1.framework.b2b.base.security.jwt.domain.identifiers.UserSessionId;
import io.tech1.framework.b2b.base.security.jwt.domain.jwt.CookieRefreshToken;
import io.tech1.framework.b2b.base.security.jwt.domain.jwt.JwtRefreshToken;
import io.tech1.framework.domain.base.Username;
import io.tech1.framework.domain.tuples.Tuple2;

import java.util.List;
import java.util.Set;

public interface AnyDbUsersSessionsRepository {
    boolean isPresent(JwtRefreshToken jwtRefreshToken);
    AnyDbUserSession requirePresence(UserSessionId sessionId);
    List<ResponseUserSession2> findByUsernameAndCookieAsSession2(Username username, CookieRefreshToken cookie);
    List<Tuple2<ResponseUserSession2, JwtRefreshToken>> findAllByCookieAsSession2(CookieRefreshToken cookie);
    List<AnyDbUserSession> findByUsernameInAsAny(Set<Username> usernames);
    AnyDbUserSession findByRefreshTokenAsAny(JwtRefreshToken jwtRefreshToken);
    UserSessionId saveAs(AnyDbUserSession userSession);
    void delete(UserSessionId sessionId);
    long deleteByUsersSessionsIds(List<UserSessionId> sessionsIds);
    void deleteByRefreshToken(JwtRefreshToken jwtRefreshToken);
}
