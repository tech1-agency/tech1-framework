package io.tech1.framework.b2b.base.security.jwt.validators.abtracts;

import io.tech1.framework.b2b.base.security.jwt.domain.identifiers.UserSessionId;
import io.tech1.framework.b2b.base.security.jwt.repositories.AnyDbUsersSessionsRepository;
import io.tech1.framework.b2b.base.security.jwt.validators.BaseUsersSessionsRequestsValidator;
import io.tech1.framework.domain.base.Username;

import static io.tech1.framework.domain.asserts.Asserts.assertNonNullOrThrow;
import static io.tech1.framework.domain.utilities.exceptions.ExceptionsMessagesUtility.accessDenied;
import static io.tech1.framework.domain.utilities.exceptions.ExceptionsMessagesUtility.invalidAttribute;

public abstract class AbstractBaseUsersSessionsRequestsValidator implements BaseUsersSessionsRequestsValidator {

    // Repositories
    private final AnyDbUsersSessionsRepository usersSessionsRepository;

    protected AbstractBaseUsersSessionsRequestsValidator(
            AnyDbUsersSessionsRepository usersSessionsRepository
    ) {
        this.usersSessionsRepository = usersSessionsRepository;
    }

    @Override
    public void validateDeleteById(Username username, UserSessionId sessionId) {
        assertNonNullOrThrow(sessionId, invalidAttribute("sessionId"));
        assertNonNullOrThrow(username, invalidAttribute("owner"));

        var session = this.usersSessionsRepository.requirePresence(sessionId);
        if (!username.equals(session.username())) {
            throw new IllegalArgumentException(accessDenied(username, "Session", sessionId.value()));
        }
    }
}
