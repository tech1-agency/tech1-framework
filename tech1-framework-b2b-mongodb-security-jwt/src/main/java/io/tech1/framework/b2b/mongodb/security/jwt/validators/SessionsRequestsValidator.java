package io.tech1.framework.b2b.mongodb.security.jwt.validators;

import io.tech1.framework.b2b.mongodb.security.jwt.domain.db.DbUser;

public interface SessionsRequestsValidator {
    void validateDeleteById(DbUser currentUser, String sessionId);
}
