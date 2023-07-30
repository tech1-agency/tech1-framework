package io.tech1.framework.b2b.base.security.jwt.services.abstracts;

import io.tech1.framework.b2b.base.security.jwt.domain.dto.responses.ResponseInvitationCodes;
import io.tech1.framework.b2b.base.security.jwt.domain.identifiers.InvitationCodeId;
import io.tech1.framework.b2b.base.security.jwt.repositories.AnyDbInvitationCodesRepository;
import io.tech1.framework.b2b.base.security.jwt.services.BaseInvitationCodesService;
import io.tech1.framework.domain.base.Username;
import io.tech1.framework.properties.ApplicationFrameworkProperties;

import static io.tech1.framework.b2b.base.security.jwt.comparators.SecurityJwtComparators.INVITATION_CODE_2;

public abstract class AbstractBaseInvitationCodesService implements BaseInvitationCodesService {

    // Repositories
    protected final AnyDbInvitationCodesRepository anyDbInvitationCodesRepository;
    // Properties
    protected final ApplicationFrameworkProperties applicationFrameworkProperties;

    protected AbstractBaseInvitationCodesService(
            AnyDbInvitationCodesRepository anyDbInvitationCodesRepository,
            ApplicationFrameworkProperties applicationFrameworkProperties
    ) {
        this.anyDbInvitationCodesRepository = anyDbInvitationCodesRepository;
        this.applicationFrameworkProperties = applicationFrameworkProperties;
    }

    @Override
    public ResponseInvitationCodes findByOwner(Username owner) {
        var invitationCodes = this.anyDbInvitationCodesRepository.findResponseCodesByOwner(owner);
        invitationCodes.sort(INVITATION_CODE_2);
        return new ResponseInvitationCodes(
                this.applicationFrameworkProperties.getSecurityJwtConfigs().getAuthoritiesConfigs().getAvailableAuthorities(),
                invitationCodes
        );
    }

    @Override
    public void deleteById(InvitationCodeId invitationCodeId) {
        this.anyDbInvitationCodesRepository.delete(invitationCodeId);
    }
}