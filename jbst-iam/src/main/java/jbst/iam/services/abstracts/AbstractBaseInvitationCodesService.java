package jbst.iam.services.abstracts;

import jbst.iam.domain.dto.requests.RequestNewInvitationCodeParams;
import jbst.iam.domain.dto.responses.ResponseInvitationCode;
import jbst.iam.domain.dto.responses.ResponseInvitationCodes;
import jbst.iam.domain.identifiers.InvitationId;
import jbst.iam.repositories.InvitationCodesRepository;
import jbst.iam.services.BaseInvitationCodesService;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import jbst.foundation.domain.base.Username;
import jbst.foundation.domain.properties.JbstProperties;

@AllArgsConstructor(access = AccessLevel.PROTECTED)
public abstract class AbstractBaseInvitationCodesService implements BaseInvitationCodesService {

    // Repositories
    protected final InvitationCodesRepository invitationCodesRepository;
    // Properties
    protected final JbstProperties jbstProperties;

    @Override
    public ResponseInvitationCodes findByOwner(Username owner) {
        var invitationCodes = this.invitationCodesRepository.findResponseCodesByOwner(owner);
        invitationCodes.sort(ResponseInvitationCode.INVITATION_CODE);
        return new ResponseInvitationCodes(
                this.jbstProperties.getSecurityJwtConfigs().getAuthoritiesConfigs().getAvailableAuthorities(),
                invitationCodes
        );
    }

    @Override
    public void save(Username owner, RequestNewInvitationCodeParams request) {
        this.invitationCodesRepository.saveAs(owner, request);
    }

    @Override
    public void deleteById(InvitationId invitationId) {
        this.invitationCodesRepository.delete(invitationId);
    }
}
