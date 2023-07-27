package io.tech1.framework.b2b.mongodb.security.jwt.validators.impl;

import io.tech1.framework.b2b.mongodb.security.jwt.domain.db.DbUser;
import io.tech1.framework.b2b.mongodb.security.jwt.repositories.UserSessionRepository;
import io.tech1.framework.b2b.mongodb.security.jwt.validators.SessionsRequestsValidator;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import static io.tech1.framework.domain.asserts.Asserts.assertNonNullNotBlankOrThrow;
import static io.tech1.framework.domain.asserts.Asserts.assertNonNullOrThrow;
import static io.tech1.framework.domain.utilities.exceptions.ExceptionsMessagesUtility.accessDenied;
import static io.tech1.framework.domain.utilities.exceptions.ExceptionsMessagesUtility.invalidAttribute;

@SuppressWarnings("SpringJavaInjectionPointsAutowiringInspection")
@Slf4j
@Component
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class SessionsRequestsValidatorImpl implements SessionsRequestsValidator {

    // Repositories
    private final UserSessionRepository userSessionRepository;

    @Override
    public void validateDeleteById(DbUser currentUser, String sessionId) {
        assertNonNullNotBlankOrThrow(sessionId, invalidAttribute("sessionId"));
        assertNonNullOrThrow(currentUser.getUsername(), invalidAttribute("owner"));

        var session = userSessionRepository.requirePresence(sessionId);
        if (!currentUser.getUsername().equals(session.getUsername())) {
            throw new IllegalArgumentException(accessDenied(currentUser.getUsername(), "Session", sessionId));
        }
    }
}
