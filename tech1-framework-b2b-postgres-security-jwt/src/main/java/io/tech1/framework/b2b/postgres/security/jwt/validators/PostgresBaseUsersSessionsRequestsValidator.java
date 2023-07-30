package io.tech1.framework.b2b.postgres.security.jwt.validators;

import io.tech1.framework.b2b.base.security.jwt.domain.identifiers.UserSessionId;
import io.tech1.framework.b2b.base.security.jwt.validators.BaseUsersSessionsRequestsValidator;
import io.tech1.framework.b2b.postgres.security.jwt.repositories.PostgresUsersSessionsRepository;
import io.tech1.framework.domain.base.Username;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import static io.tech1.framework.domain.asserts.Asserts.assertNonNullOrThrow;
import static io.tech1.framework.domain.utilities.exceptions.ExceptionsMessagesUtility.accessDenied;
import static io.tech1.framework.domain.utilities.exceptions.ExceptionsMessagesUtility.invalidAttribute;

@Slf4j
@Component
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class PostgresBaseUsersSessionsRequestsValidator implements BaseUsersSessionsRequestsValidator {

    // Repositories
    private final PostgresUsersSessionsRepository postgresUsersSessionsRepository;

    @Override
    public void validateDeleteById(Username username, UserSessionId sessionId) {
        assertNonNullOrThrow(sessionId, invalidAttribute("sessionId"));
        assertNonNullOrThrow(username, invalidAttribute("owner"));

        var session = this.postgresUsersSessionsRepository.requirePresence(sessionId);
        if (!username.equals(session.getUsername())) {
            throw new IllegalArgumentException(accessDenied(username, "Session", sessionId.value()));
        }
    }
}
