package io.tech1.framework.b2b.mongodb.security.jwt.services;

import io.tech1.framework.b2b.base.security.jwt.domain.events.EventSessionAddUserRequestMetadata;
import io.tech1.framework.b2b.base.security.jwt.domain.identifiers.UserSessionId;
import io.tech1.framework.b2b.base.security.jwt.domain.jwt.CookieRefreshToken;
import io.tech1.framework.b2b.base.security.jwt.domain.jwt.JwtRefreshToken;
import io.tech1.framework.b2b.base.security.jwt.domain.jwt.JwtUser;
import io.tech1.framework.b2b.base.security.jwt.domain.sessions.SessionsValidatedTuple2;
import io.tech1.framework.b2b.mongodb.security.jwt.domain.db.MongoDbUserSession;
import io.tech1.framework.domain.base.Username;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

public interface UserSessionService {
    MongoDbUserSession save(JwtUser user, JwtRefreshToken jwtRefreshToken, HttpServletRequest httpServletRequest);
    MongoDbUserSession refresh(JwtUser user, JwtRefreshToken oldJwtRefreshToken, JwtRefreshToken newJwtRefreshToken, HttpServletRequest httpServletRequest);
    MongoDbUserSession saveUserRequestMetadata(EventSessionAddUserRequestMetadata event);
    SessionsValidatedTuple2 validate(List<MongoDbUserSession> usersSessions);
    void deleteById(UserSessionId sessionId);
    void deleteAllExceptCurrent(Username username, CookieRefreshToken cookie);
    void deleteAllExceptCurrentAsSuperuser(CookieRefreshToken cookie);
}
