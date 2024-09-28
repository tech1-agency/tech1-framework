package io.tech1.framework.iam.services;

import io.tech1.framework.iam.domain.db.UserSession;
import io.tech1.framework.iam.domain.events.EventSessionUserRequestMetadataAdd;
import io.tech1.framework.iam.domain.events.EventSessionUserRequestMetadataRenew;
import io.tech1.framework.iam.domain.functions.FunctionSessionUserRequestMetadataSave;
import io.tech1.framework.iam.domain.identifiers.UserSessionId;
import io.tech1.framework.iam.domain.jwt.JwtAccessToken;
import io.tech1.framework.iam.domain.jwt.JwtRefreshToken;
import io.tech1.framework.iam.domain.jwt.JwtUser;
import io.tech1.framework.iam.domain.jwt.RequestAccessToken;
import io.tech1.framework.iam.domain.sessions.SessionsExpiredTable;
import tech1.framework.foundation.domain.base.Username;

import jakarta.servlet.http.HttpServletRequest;
import java.util.Set;

public interface BaseUsersSessionsService {
    void assertAccess(Username username, UserSessionId sessionId);
    void save(JwtUser user, JwtAccessToken accessToken, JwtRefreshToken refreshToken, HttpServletRequest httpServletRequest);
    void refresh(JwtUser user, UserSession oldSession, JwtAccessToken newAccessToken, JwtRefreshToken newRefreshToken, HttpServletRequest httpServletRequest);
    UserSession saveUserRequestMetadata(EventSessionUserRequestMetadataAdd event);
    void saveUserRequestMetadata(EventSessionUserRequestMetadataRenew event);
    UserSession saveUserRequestMetadata(FunctionSessionUserRequestMetadataSave saveFunction);
    SessionsExpiredTable getExpiredRefreshTokensSessions(Set<Username> usernames);
    void enableUserRequestMetadataRenewCron();
    void enableUserRequestMetadataRenewManually(UserSessionId sessionId);
    void renewUserRequestMetadata(UserSession session, HttpServletRequest httpServletRequest);
    void deleteById(UserSessionId sessionId);
    void deleteAllExceptCurrent(Username username, RequestAccessToken requestAccessToken);
    void deleteAllExceptCurrentAsSuperuser(RequestAccessToken requestAccessToken);
}
