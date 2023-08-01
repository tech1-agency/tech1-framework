package io.tech1.framework.b2b.base.security.jwt.validators.abtracts;

import io.tech1.framework.b2b.base.security.jwt.domain.dto.requests.RequestNewInvitationCodeParams;
import io.tech1.framework.b2b.base.security.jwt.domain.identifiers.InvitationCodeId;
import io.tech1.framework.b2b.base.security.jwt.repositories.AnyDbInvitationCodesRepository;
import io.tech1.framework.b2b.base.security.jwt.validators.BaseInvitationCodesRequestsValidator;
import io.tech1.framework.domain.base.Username;
import io.tech1.framework.properties.ApplicationFrameworkProperties;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;

import static io.tech1.framework.domain.asserts.Asserts.*;
import static io.tech1.framework.domain.utilities.exceptions.ExceptionsMessagesUtility.accessDenied;
import static io.tech1.framework.domain.utilities.exceptions.ExceptionsMessagesUtility.invalidAttribute;

@AllArgsConstructor(access = AccessLevel.PROTECTED)
public abstract class AbstractBaseInvitationCodesRequestsValidator implements BaseInvitationCodesRequestsValidator {

    // Repositories
    protected final AnyDbInvitationCodesRepository anyDbInvitationCodesRepository;
    // Properties
    protected final ApplicationFrameworkProperties applicationFrameworkProperties;

    @Override
    public void validateCreateNewInvitationCode(RequestNewInvitationCodeParams requestNewInvitationCodeParams) {
        var authorities = requestNewInvitationCodeParams.authorities();
        var availableAuthorities = this.applicationFrameworkProperties.getSecurityJwtConfigs().getAuthoritiesConfigs().getAvailableAuthorities();

        assertNonNullNotEmptyOrThrow(authorities, invalidAttribute("authorities"));
        assertTrueOrThrow(availableAuthorities.containsAll(authorities), "Invitation code request params contains unsupported authority");
    }

    @Override
    public void validateDeleteById(Username username, InvitationCodeId invitationCodeId) {
        assertNonNullOrThrow(invitationCodeId, invalidAttribute("invitationCodeId"));
        assertNonNullOrThrow(username, invalidAttribute("owner"));

        var invitationCode = this.anyDbInvitationCodesRepository.requirePresence(invitationCodeId);
        if (!username.equals(invitationCode.owner())) {
            throw new IllegalArgumentException(accessDenied(username, "InvitationCode", invitationCodeId.value()));
        }
    }
}
