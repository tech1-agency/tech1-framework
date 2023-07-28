package io.tech1.framework.b2b.mongodb.security.jwt.validators;

import io.tech1.framework.b2b.mongodb.security.jwt.repositories.UserSessionRepository;
import io.tech1.framework.b2b.base.security.jwt.validators.SessionsRequestsValidator;
import io.tech1.framework.domain.base.Username;
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
public class MongodbSessionsRequestsValidator implements SessionsRequestsValidator {

    // Repositories
    private final UserSessionRepository userSessionRepository;

    @Override
    public void validateDeleteById(Username username, String sessionId) {
        assertNonNullNotBlankOrThrow(sessionId, invalidAttribute("sessionId"));
        assertNonNullOrThrow(username, invalidAttribute("owner"));

        var session = this.userSessionRepository.requirePresence(sessionId);
        if (!username.equals(session.getUsername())) {
            throw new IllegalArgumentException(accessDenied(username, "Session", sessionId));
        }
    }
}
