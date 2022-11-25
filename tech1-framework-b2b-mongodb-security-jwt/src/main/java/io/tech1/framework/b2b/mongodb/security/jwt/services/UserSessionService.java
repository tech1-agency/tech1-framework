package io.tech1.framework.b2b.mongodb.security.jwt.services;

import io.tech1.framework.b2b.mongodb.security.jwt.domain.db.DbUser;
import io.tech1.framework.b2b.mongodb.security.jwt.domain.db.DbUserSession;
import io.tech1.framework.b2b.mongodb.security.jwt.domain.events.EventSessionAddUserRequestMetadata;
import io.tech1.framework.b2b.mongodb.security.jwt.domain.jwt.JwtRefreshToken;
import io.tech1.framework.b2b.mongodb.security.jwt.domain.session.SessionsValidatedTuple2;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

public interface UserSessionService {
    DbUserSession save(DbUser user, JwtRefreshToken jwtRefreshToken, HttpServletRequest httpServletRequest);
    DbUserSession refresh(DbUser user, JwtRefreshToken oldJwtRefreshToken, JwtRefreshToken newJwtRefreshToken, HttpServletRequest httpServletRequest);
    DbUserSession saveUserRequestMetadata(EventSessionAddUserRequestMetadata event);
    SessionsValidatedTuple2 validate(List<DbUserSession> usersSessions);
}
