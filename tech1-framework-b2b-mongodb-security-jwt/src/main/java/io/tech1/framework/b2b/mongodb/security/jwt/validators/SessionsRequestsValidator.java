package io.tech1.framework.b2b.mongodb.security.jwt.validators;

import io.tech1.framework.domain.base.Username;

public interface SessionsRequestsValidator {
    void validateDeleteById(Username username, String sessionId);
}
