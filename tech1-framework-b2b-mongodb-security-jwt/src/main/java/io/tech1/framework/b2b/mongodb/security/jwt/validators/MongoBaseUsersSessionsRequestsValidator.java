package io.tech1.framework.b2b.mongodb.security.jwt.validators;

import io.tech1.framework.b2b.base.security.jwt.domain.identifiers.UserSessionId;
import io.tech1.framework.b2b.base.security.jwt.validators.BaseUsersSessionsRequestsValidator;
import io.tech1.framework.b2b.mongodb.security.jwt.repositories.MongoUsersSessionsRepository;
import io.tech1.framework.domain.base.Username;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import static io.tech1.framework.domain.asserts.Asserts.assertNonNullOrThrow;
import static io.tech1.framework.domain.utilities.exceptions.ExceptionsMessagesUtility.accessDenied;
import static io.tech1.framework.domain.utilities.exceptions.ExceptionsMessagesUtility.invalidAttribute;

@SuppressWarnings("SpringJavaInjectionPointsAutowiringInspection")
@Slf4j
@Component
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class MongoBaseUsersSessionsRequestsValidator implements BaseUsersSessionsRequestsValidator {

    // Repositories
    private final MongoUsersSessionsRepository mongoUsersSessionsRepository;

    @Override
    public void validateDeleteById(Username username, UserSessionId sessionId) {
        assertNonNullOrThrow(sessionId, invalidAttribute("sessionId"));
        assertNonNullOrThrow(username, invalidAttribute("owner"));

        var session = this.mongoUsersSessionsRepository.requirePresence(sessionId);
        if (!username.equals(session.getUsername())) {
            throw new IllegalArgumentException(accessDenied(username, "Session", sessionId.value()));
        }
    }
}
