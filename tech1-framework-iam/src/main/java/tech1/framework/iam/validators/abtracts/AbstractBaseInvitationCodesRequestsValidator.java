package tech1.framework.iam.validators.abtracts;

import tech1.framework.iam.domain.dto.requests.RequestNewInvitationCodeParams;
import tech1.framework.iam.domain.identifiers.InvitationCodeId;
import tech1.framework.iam.repositories.InvitationCodesRepository;
import tech1.framework.foundation.domain.base.Username;
import tech1.framework.foundation.domain.properties.ApplicationFrameworkProperties;
import tech1.framework.iam.validators.BaseInvitationCodesRequestsValidator;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import org.springframework.security.access.AccessDeniedException;

import static tech1.framework.foundation.domain.asserts.Asserts.assertTrueOrThrow;
import static tech1.framework.foundation.utilities.collections.CollectionUtility.baseJoiningRaw;
import static tech1.framework.foundation.utilities.exceptions.ExceptionsMessagesUtility.entityAccessDenied;
import static tech1.framework.foundation.utilities.exceptions.ExceptionsMessagesUtility.entityNotFound;

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
