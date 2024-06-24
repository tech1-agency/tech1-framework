package io.tech1.framework.b2b.base.security.jwt.validators.abtracts;

import io.tech1.framework.b2b.base.security.jwt.domain.dto.requests.RequestNewInvitationCodeParams;
import io.tech1.framework.b2b.base.security.jwt.domain.identifiers.InvitationCodeId;
import io.tech1.framework.b2b.base.security.jwt.repositories.InvitationCodesRepository;
import io.tech1.framework.b2b.base.security.jwt.validators.BaseInvitationCodesRequestsValidator;
import io.tech1.framework.domain.base.Username;
import io.tech1.framework.domain.properties.ApplicationFrameworkProperties;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import org.springframework.security.access.AccessDeniedException;

import static io.tech1.framework.domain.asserts.Asserts.assertTrueOrThrow;
import static io.tech1.framework.foundation.utilities.collections.CollectionUtility.baseJoiningRaw;
import static io.tech1.framework.foundation.utilities.exceptions.ExceptionsMessagesUtility.entityAccessDenied;
import static io.tech1.framework.foundation.utilities.exceptions.ExceptionsMessagesUtility.entityNotFound;

@AllArgsConstructor(access = AccessLevel.PROTECTED)
public abstract class AbstractBaseInvitationCodesRequestsValidator implements BaseInvitationCodesRequestsValidator {

    // Repositories
    protected final InvitationCodesRepository invitationCodesRepository;
    // Properties
    protected final ApplicationFrameworkProperties applicationFrameworkProperties;

    @Override
    public void validateCreateNewInvitationCode(RequestNewInvitationCodeParams request) {
        var availableAuthorities = this.applicationFrameworkProperties.getSecurityJwtConfigs().getAuthoritiesConfigs().getAvailableAuthorities();
        assertTrueOrThrow(
                availableAuthorities.containsAll(request.authorities()),
                "Authorities must contains: [%s]".formatted(baseJoiningRaw(availableAuthorities))
        );
    }

    @Override
    public void validateDeleteById(Username username, InvitationCodeId invitationCodeId) {
        var tuplePresence = this.invitationCodesRepository.isPresent(invitationCodeId);
        if (!tuplePresence.present()) {
            throw new IllegalArgumentException(entityNotFound("Invitation code", invitationCodeId.value()));
        }
        if (!username.equals(tuplePresence.value().owner())) {
            throw new AccessDeniedException(entityAccessDenied("Invitation code", invitationCodeId.value()));
        }
    }
}
