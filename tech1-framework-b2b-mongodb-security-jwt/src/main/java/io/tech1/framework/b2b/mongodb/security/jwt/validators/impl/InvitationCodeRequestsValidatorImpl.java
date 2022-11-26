package io.tech1.framework.b2b.mongodb.security.jwt.validators.impl;

import io.tech1.framework.b2b.mongodb.security.jwt.domain.db.DbUser;
import io.tech1.framework.b2b.mongodb.security.jwt.domain.dto.requests.RequestNewInvitationCodeParams;
import io.tech1.framework.b2b.mongodb.security.jwt.repositories.InvitationCodeRepository;
import io.tech1.framework.b2b.mongodb.security.jwt.validators.InvitationCodeRequestsValidator;
import io.tech1.framework.properties.ApplicationFrameworkProperties;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import static io.tech1.framework.domain.asserts.Asserts.*;
import static io.tech1.framework.domain.utilities.exceptions.ExceptionsMessagesUtility.accessDenied;
import static io.tech1.framework.domain.utilities.exceptions.ExceptionsMessagesUtility.invalidAttribute;

@Slf4j
@Component
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class InvitationCodeRequestsValidatorImpl implements InvitationCodeRequestsValidator {

    // Repositories
    private final InvitationCodeRepository invitationCodeRepository;
    // Properties
    private final ApplicationFrameworkProperties applicationFrameworkProperties;

    @Override
    public void validateCreateNewInvitationCode(RequestNewInvitationCodeParams requestNewInvitationCodeParams) {
        var authorities = requestNewInvitationCodeParams.getAuthorities();
        var availableAuthorities = this.applicationFrameworkProperties.getSecurityJwtConfigs().getAuthoritiesConfigs().getAvailableAuthorities();

        assertNonNullNotEmptyOrThrow(authorities, invalidAttribute("authorities"));
        assertTrueOrThrow(availableAuthorities.containsAll(authorities), "Invitation code request params contains unsupported authority");
    }

    @Override
    public void validateDeleteById(DbUser currentUser, String invitationCodeId) {
        assertNonNullNotBlankOrThrow(invitationCodeId, invalidAttribute("invitationCodeId"));
        assertNonNullOrThrow(currentUser.getUsername(), invalidAttribute("owner"));

        var invitationCode = invitationCodeRepository.requirePresence(invitationCodeId);
        if (!currentUser.getUsername().equals(invitationCode.getOwner())) {
            throw new IllegalArgumentException(accessDenied(currentUser.getUsername(), "InvitationCode", invitationCodeId));
        }
    }
}
