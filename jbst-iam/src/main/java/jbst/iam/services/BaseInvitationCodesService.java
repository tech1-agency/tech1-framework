package jbst.iam.services;

import jbst.iam.domain.dto.requests.RequestNewInvitationCodeParams;
import jbst.iam.domain.dto.responses.ResponseInvitationCodes;
import jbst.iam.domain.identifiers.InvitationId;
import jbst.foundation.domain.base.Username;

public interface BaseInvitationCodesService {
    ResponseInvitationCodes findByOwner(Username owner);
    void save(Username owner, RequestNewInvitationCodeParams request);
    void deleteById(InvitationId invitationId);
}
