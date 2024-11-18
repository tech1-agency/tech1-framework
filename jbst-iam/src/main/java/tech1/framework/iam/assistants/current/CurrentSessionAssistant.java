package tech1.framework.iam.assistants.current;

import tech1.framework.iam.domain.db.UserSession;
import tech1.framework.iam.domain.dto.responses.ResponseUserSessionsTable;
import tech1.framework.iam.domain.jwt.JwtUser;
import tech1.framework.iam.domain.jwt.RequestAccessToken;
import tech1.framework.iam.domain.security.CurrentClientUser;
import tech1.framework.foundation.domain.base.Username;
import tech1.framework.foundation.domain.exceptions.tokens.AccessTokenNotFoundException;

import jakarta.servlet.http.HttpServletRequest;

public interface CurrentSessionAssistant {
    Username getCurrentUsername();
    JwtUser getCurrentJwtUser();
    CurrentClientUser getCurrentClientUser();
    UserSession getCurrentUserSession(HttpServletRequest httpServletRequest) throws AccessTokenNotFoundException;
    ResponseUserSessionsTable getCurrentUserDbSessionsTable(RequestAccessToken requestAccessToken);
}
