package io.tech1.framework.iam.services.abstracts;

import io.tech1.framework.iam.domain.dto.requests.RequestNewInvitationCodeParams;
import io.tech1.framework.iam.domain.dto.responses.ResponseInvitationCode;
import io.tech1.framework.iam.domain.dto.responses.ResponseInvitationCodes;
import io.tech1.framework.iam.domain.identifiers.InvitationCodeId;
import io.tech1.framework.iam.repositories.InvitationCodesRepository;
import io.tech1.framework.iam.services.BaseInvitationCodesService;
import io.tech1.framework.foundation.domain.base.Username;
import io.tech1.framework.foundation.domain.properties.ApplicationFrameworkProperties;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;

@AllArgsConstructor(access = AccessLevel.PROTECTED)
public abstract class AbstractBaseInvitationCodesService implements BaseInvitationCodesService {

    // Repositories
    protected final InvitationCodesRepository invitationCodesRepository;
    // Properties
    protected final ApplicationFrameworkProperties applicationFrameworkProperties;

    @Override
    public ResponseInvitationCodes findByOwner(Username owner) {
        var invitationCodes = this.invitationCodesRepository.findResponseCodesByOwner(owner);
        invitationCodes.sort(ResponseInvitationCode.INVITATION_CODE);
        return new ResponseInvitationCodes(
                this.applicationFrameworkProperties.getSecurityJwtConfigs().getAuthoritiesConfigs().getAvailableAuthorities(),
                invitationCodes
        );
    }

    @Override
    public void save(Username owner, RequestNewInvitationCodeParams request) {
        this.invitationCodesRepository.saveAs(owner, request);
    }

    @Override
    public void deleteById(InvitationCodeId invitationCodeId) {
        this.invitationCodesRepository.delete(invitationCodeId);
    }
}
