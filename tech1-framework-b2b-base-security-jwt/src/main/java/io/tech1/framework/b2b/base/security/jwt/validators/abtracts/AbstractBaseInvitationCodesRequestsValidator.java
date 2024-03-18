package io.tech1.framework.b2b.base.security.jwt.validators.abtracts;

import io.tech1.framework.b2b.base.security.jwt.domain.dto.requests.RequestNewInvitationCodeParams;
import io.tech1.framework.b2b.base.security.jwt.domain.identifiers.InvitationCodeId;
import io.tech1.framework.b2b.base.security.jwt.repositories.InvitationCodesRepository;
import io.tech1.framework.b2b.base.security.jwt.validators.BaseInvitationCodesRequestsValidator;
import io.tech1.framework.domain.base.Username;
import io.tech1.framework.properties.ApplicationFrameworkProperties;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;

import static io.tech1.framework.domain.asserts.Asserts.*;
import static io.tech1.framework.domain.utilities.exceptions.ExceptionsMessagesUtility.*;

@AllArgsConstructor(access = AccessLevel.PROTECTED)
public abstract class AbstractBaseInvitationCodesRequestsValidator implements BaseInvitationCodesRequestsValidator {

    // Repositories
    protected final InvitationCodesRepository invitationCodesRepository;
    // Properties
    protected final ApplicationFrameworkProperties applicationFrameworkProperties;

    @Override
    public void validateCreateNewInvitationCode(RequestNewInvitationCodeParams request) {
        var authorities = request.authorities();
        var availableAuthorities = this.applicationFrameworkProperties.getSecurityJwtConfigs().getAuthoritiesConfigs().getAvailableAuthorities();

        assertTrueOrThrow(availableAuthorities.containsAll(authorities), "Invitation code request params contains unsupported authority");
    }

    @Override
    public void validateDeleteById(Username username, InvitationCodeId invitationCodeId) {
        assertNonNullOrThrow(invitationCodeId, invalidAttribute("invitationCodeId"));
        assertNonNullOrThrow(username, invalidAttribute("owner"));

        var tuplePresence = this.invitationCodesRepository.isPresent(invitationCodeId);

        if (!tuplePresence.present()) {
            throw new IllegalArgumentException(entityNotFound("InvitationCode", invitationCodeId.value()));
        }

        if (!username.equals(tuplePresence.value().owner())) {
            throw new IllegalArgumentException(entityAccessDenied("InvitationCode", invitationCodeId.value()));
        }
    }
}
