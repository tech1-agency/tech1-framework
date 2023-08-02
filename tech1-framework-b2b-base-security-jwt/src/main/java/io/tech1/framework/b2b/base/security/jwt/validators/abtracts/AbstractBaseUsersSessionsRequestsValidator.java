package io.tech1.framework.b2b.base.security.jwt.validators.abtracts;

import io.tech1.framework.b2b.base.security.jwt.domain.identifiers.UserSessionId;
import io.tech1.framework.b2b.base.security.jwt.repositories.AnyDbUsersSessionsRepository;
import io.tech1.framework.b2b.base.security.jwt.validators.BaseUsersSessionsRequestsValidator;
import io.tech1.framework.domain.base.Username;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;

import static io.tech1.framework.domain.asserts.Asserts.assertNonNullOrThrow;
import static io.tech1.framework.domain.utilities.exceptions.ExceptionsMessagesUtility.entityAccessDenied;
import static io.tech1.framework.domain.utilities.exceptions.ExceptionsMessagesUtility.invalidAttribute;

@AllArgsConstructor(access = AccessLevel.PROTECTED)
public abstract class AbstractBaseUsersSessionsRequestsValidator implements BaseUsersSessionsRequestsValidator {

    // Repositories
    protected final AnyDbUsersSessionsRepository anyDbUsersSessionsRepository;

    @Override
    public void validateDeleteById(Username username, UserSessionId sessionId) {
        assertNonNullOrThrow(sessionId, invalidAttribute("sessionId"));
        assertNonNullOrThrow(username, invalidAttribute("owner"));

        var session = this.anyDbUsersSessionsRepository.requirePresence(sessionId);
        if (!username.equals(session.username())) {
            throw new IllegalArgumentException(entityAccessDenied("Session", sessionId.value()));
        }
    }
}
