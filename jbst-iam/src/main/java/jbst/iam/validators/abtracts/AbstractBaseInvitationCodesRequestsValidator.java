package jbst.iam.validators.abtracts;

import jbst.iam.domain.dto.requests.RequestNewInvitationCodeParams;
import jbst.iam.domain.identifiers.InvitationCodeId;
import jbst.iam.repositories.InvitationCodesRepository;
import jbst.iam.validators.BaseInvitationCodesRequestsValidator;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import org.springframework.security.access.AccessDeniedException;
import jbst.foundation.domain.base.Username;
import jbst.foundation.domain.properties.JbstProperties;

import static jbst.foundation.domain.asserts.Asserts.assertTrueOrThrow;
import static jbst.foundation.utilities.collections.CollectionUtility.baseJoiningRaw;
import static jbst.foundation.utilities.exceptions.ExceptionsMessagesUtility.entityAccessDenied;
import static jbst.foundation.utilities.exceptions.ExceptionsMessagesUtility.entityNotFound;

@AllArgsConstructor(access = AccessLevel.PROTECTED)
public abstract class AbstractBaseInvitationCodesRequestsValidator implements BaseInvitationCodesRequestsValidator {

    // Repositories
    protected final InvitationCodesRepository invitationCodesRepository;
    // Properties
    protected final JbstProperties jbstProperties;

    @Override
    public void validateCreateNewInvitationCode(RequestNewInvitationCodeParams request) {
        var availableAuthorities = this.jbstProperties.getSecurityJwtConfigs().getAuthoritiesConfigs().getAvailableAuthorities();
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
