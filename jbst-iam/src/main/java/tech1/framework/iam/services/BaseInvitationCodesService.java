package tech1.framework.iam.services;

import tech1.framework.iam.domain.dto.requests.RequestNewInvitationCodeParams;
import tech1.framework.iam.domain.dto.responses.ResponseInvitationCodes;
import tech1.framework.iam.domain.identifiers.InvitationCodeId;
import tech1.framework.foundation.domain.base.Username;

public interface BaseInvitationCodesService {
    ResponseInvitationCodes findByOwner(Username owner);
    void save(Username owner, RequestNewInvitationCodeParams request);
    void deleteById(InvitationCodeId invitationCodeId);
}
