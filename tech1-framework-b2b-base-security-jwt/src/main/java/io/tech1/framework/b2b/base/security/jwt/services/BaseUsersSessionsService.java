package io.tech1.framework.b2b.base.security.jwt.services;

import io.tech1.framework.b2b.base.security.jwt.domain.events.EventSessionAddUserRequestMetadata;
import io.tech1.framework.b2b.base.security.jwt.domain.identifiers.UserSessionId;
import io.tech1.framework.b2b.base.security.jwt.domain.jwt.CookieAccessToken;
import io.tech1.framework.b2b.base.security.jwt.domain.jwt.JwtAccessToken;
import io.tech1.framework.b2b.base.security.jwt.domain.jwt.JwtRefreshToken;
import io.tech1.framework.b2b.base.security.jwt.domain.jwt.JwtUser;
import io.tech1.framework.b2b.base.security.jwt.domain.sessions.SessionsExpiredTable;
import io.tech1.framework.domain.base.Username;
import io.tech1.framework.domain.http.requests.UserRequestMetadata;
import io.tech1.framework.domain.tuples.Tuple2;

import javax.servlet.http.HttpServletRequest;
import java.util.Set;

public interface BaseUsersSessionsService {
    void save(JwtUser user, JwtAccessToken accessToken, JwtRefreshToken refreshToken, HttpServletRequest httpServletRequest);
    void refresh(JwtUser user, JwtRefreshToken oldRefreshToken, JwtAccessToken newAccessToken, JwtRefreshToken newRefreshToken, HttpServletRequest httpServletRequest);
    Tuple2<UserSessionId, UserRequestMetadata> saveUserRequestMetadata(EventSessionAddUserRequestMetadata event);
    SessionsExpiredTable getExpiredRefreshTokensSessions(Set<Username> usernames);
    void deleteById(UserSessionId sessionId);
    void deleteAllExceptCurrent(Username username, CookieAccessToken cookie);
    void deleteAllExceptCurrentAsSuperuser(CookieAccessToken cookie);
}
