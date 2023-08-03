package io.tech1.framework.b2b.base.security.jwt.services.abstracts;

import io.tech1.framework.b2b.base.security.jwt.domain.dto.requests.RequestNewInvitationCodeParams;
import io.tech1.framework.b2b.base.security.jwt.domain.dto.responses.ResponseInvitationCodes;
import io.tech1.framework.b2b.base.security.jwt.domain.identifiers.InvitationCodeId;
import io.tech1.framework.b2b.base.security.jwt.repositories.InvitationCodesRepository;
import io.tech1.framework.b2b.base.security.jwt.services.BaseInvitationCodesService;
import io.tech1.framework.domain.base.Username;
import io.tech1.framework.properties.ApplicationFrameworkProperties;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;

import static io.tech1.framework.b2b.base.security.jwt.comparators.SecurityJwtComparators.INVITATION_CODE_2;

@AllArgsConstructor(access = AccessLevel.PROTECTED)
public abstract class AbstractBaseInvitationCodesService implements BaseInvitationCodesService {

    // Repositories
    protected final InvitationCodesRepository invitationCodesRepository;
    // Properties
    protected final ApplicationFrameworkProperties applicationFrameworkProperties;

    @Override
    public ResponseInvitationCodes findByOwner(Username owner) {
        var invitationCodes = this.invitationCodesRepository.findResponseCodesByOwner(owner);
        invitationCodes.sort(INVITATION_CODE_2);
        return new ResponseInvitationCodes(
                this.applicationFrameworkProperties.getSecurityJwtConfigs().getAuthoritiesConfigs().getAvailableAuthorities(),
                invitationCodes
        );
    }

    @Override
    public void save(Username owner, RequestNewInvitationCodeParams requestNewInvitationCodeParams) {
        this.invitationCodesRepository.saveAs(owner, requestNewInvitationCodeParams);
    }

    @Override
    public void deleteById(InvitationCodeId invitationCodeId) {
        this.invitationCodesRepository.delete(invitationCodeId);
    }
}
