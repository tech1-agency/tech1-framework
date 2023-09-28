package io.tech1.framework.b2b.base.security.jwt.validators;

import io.tech1.framework.b2b.base.security.jwt.domain.identifiers.UserSessionId;
import io.tech1.framework.domain.base.Username;

public interface BaseUsersSessionsRequestsValidator {
    void validateAccess(Username username, UserSessionId sessionId);
}
