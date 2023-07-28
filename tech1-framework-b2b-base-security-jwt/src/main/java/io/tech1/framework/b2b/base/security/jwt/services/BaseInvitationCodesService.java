package io.tech1.framework.b2b.base.security.jwt.services;

import io.tech1.framework.b2b.base.security.jwt.domain.dto.requests.RequestNewInvitationCodeParams;
import io.tech1.framework.b2b.base.security.jwt.domain.dto.responses.ResponseInvitationCodes;
import io.tech1.framework.b2b.base.security.jwt.domain.identifiers.InvitationCodeId;
import io.tech1.framework.domain.base.Username;

public interface BaseInvitationCodesService {
    ResponseInvitationCodes findByOwner(Username owner);
    void save(RequestNewInvitationCodeParams requestNewInvitationCodeParams, Username owner);
    void deleteById(InvitationCodeId invitationCodeId);
}
