package io.tech1.framework.b2b.base.security.jwt.validators;

import io.tech1.framework.domain.base.Username;

public interface SessionsRequestsValidator {
    void validateDeleteById(Username username, String sessionId);
}
