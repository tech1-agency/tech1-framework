package io.tech1.framework.b2b.mongodb.security.jwt.services;

import io.tech1.framework.b2b.mongodb.security.jwt.domain.db.DbUser;
import io.tech1.framework.b2b.mongodb.security.jwt.domain.db.DbUserSession;
import io.tech1.framework.b2b.mongodb.security.jwt.domain.events.EventSessionAddUserRequestMetadata;
import io.tech1.framework.b2b.mongodb.security.jwt.domain.jwt.JwtRefreshToken;
import io.tech1.framework.b2b.mongodb.security.jwt.domain.session.SessionsValidatedTuple2;
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
    DbUserSession save(DbUser user, JwtRefreshToken jwtRefreshToken, HttpServletRequest httpServletRequest);
    DbUserSession refresh(DbUser user, JwtRefreshToken oldJwtRefreshToken, JwtRefreshToken newJwtRefreshToken, HttpServletRequest httpServletRequest);
    DbUserSession saveUserRequestMetadata(EventSessionAddUserRequestMetadata event);
    SessionsValidatedTuple2 validate(List<DbUserSession> usersSessions);
}
