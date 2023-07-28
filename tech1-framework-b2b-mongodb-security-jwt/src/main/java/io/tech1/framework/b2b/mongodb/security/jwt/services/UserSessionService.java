package io.tech1.framework.b2b.mongodb.security.jwt.services;

import io.tech1.framework.b2b.base.security.jwt.domain.jwt.CookieRefreshToken;
import io.tech1.framework.b2b.base.security.jwt.domain.jwt.JwtRefreshToken;
import io.tech1.framework.b2b.base.security.jwt.domain.sessions.SessionsValidatedTuple2;
import io.tech1.framework.b2b.mongodb.security.jwt.domain.db.DbUserSession;
import io.tech1.framework.b2b.mongodb.security.jwt.domain.events.EventSessionAddUserRequestMetadata;
import io.tech1.framework.b2b.base.security.jwt.domain.jwt.JwtUser;
import io.tech1.framework.domain.base.Username;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Set;

public interface UserSessionService {
    // proxy-methods
    List<DbUserSession> findByUsername(Username username);
    List<DbUserSession> findByUsernameIn(Set<Username> usernames);
    Long deleteByIdIn(List<String> ids);
    DbUserSession findByRefreshToken(JwtRefreshToken jwtRefreshToken);
    void deleteByRefreshToken(JwtRefreshToken jwtRefreshToken);
    // service-methods
    DbUserSession save(JwtUser user, JwtRefreshToken jwtRefreshToken, HttpServletRequest httpServletRequest);
    DbUserSession refresh(JwtUser user, JwtRefreshToken oldJwtRefreshToken, JwtRefreshToken newJwtRefreshToken, HttpServletRequest httpServletRequest);
    DbUserSession saveUserRequestMetadata(EventSessionAddUserRequestMetadata event);
    SessionsValidatedTuple2 validate(List<DbUserSession> usersSessions);
    void deleteById(String sessionId);
    void deleteAllExceptCurrent(Username username, CookieRefreshToken cookie);
    void deleteAllExceptCurrentAsSuperuser(CookieRefreshToken cookie);
}
