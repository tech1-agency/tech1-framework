package jbst.iam.services.abstracts;

import jbst.iam.domain.dto.requests.RequestNewInvitationParams;
import jbst.iam.domain.dto.responses.ResponseInvitation;
import jbst.iam.domain.dto.responses.ResponseInvitations;
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
    public ResponseInvitations findByOwner(Username owner) {
        var invitationCodes = this.invitationCodesRepository.findResponseCodesByOwner(owner);
        invitationCodes.sort(ResponseInvitation.INVITATION);
        return new ResponseInvitations(
                this.jbstProperties.getSecurityJwtConfigs().getAuthoritiesConfigs().getAvailableAuthorities(),
                invitationCodes
        );
    }

    @Override
    public void save(Username owner, RequestNewInvitationParams request) {
        this.invitationCodesRepository.saveAs(owner, request);
    }

    @Override
    public void deleteById(InvitationId invitationId) {
        this.invitationCodesRepository.delete(invitationId);
    }
}
