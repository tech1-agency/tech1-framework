package io.tech1.framework.iam.services;

import io.tech1.framework.iam.domain.dto.requests.RequestNewInvitationCodeParams;
import io.tech1.framework.iam.domain.dto.responses.ResponseInvitationCodes;
import io.tech1.framework.iam.domain.identifiers.InvitationCodeId;
import io.tech1.framework.foundation.domain.base.Username;

public interface BaseInvitationCodesService {
    ResponseInvitationCodes findByOwner(Username owner);
    void save(Username owner, RequestNewInvitationCodeParams request);
    void deleteById(InvitationCodeId invitationCodeId);
}
