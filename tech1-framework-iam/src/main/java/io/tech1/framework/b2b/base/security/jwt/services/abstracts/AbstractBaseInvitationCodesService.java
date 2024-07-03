package io.tech1.framework.b2b.base.security.jwt.services.abstracts;

import io.tech1.framework.b2b.base.security.jwt.domain.dto.requests.RequestNewInvitationCodeParams;
import io.tech1.framework.b2b.base.security.jwt.domain.dto.responses.ResponseInvitationCode;
import io.tech1.framework.b2b.base.security.jwt.domain.dto.responses.ResponseInvitationCodes;
import io.tech1.framework.b2b.base.security.jwt.domain.identifiers.InvitationCodeId;
import io.tech1.framework.b2b.base.security.jwt.repositories.InvitationCodesRepository;
import io.tech1.framework.b2b.base.security.jwt.services.BaseInvitationCodesService;
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
