package io.tech1.framework.b2b.mongodb.security.jwt.validators;

import io.tech1.framework.b2b.base.security.jwt.domain.dto.requests.RequestNewInvitationCodeParams;
import io.tech1.framework.domain.base.Username;

public interface InvitationCodeRequestsValidator {
    void validateCreateNewInvitationCode(RequestNewInvitationCodeParams requestNewInvitationCodeParams);
    void validateDeleteById(Username username, String invitationCodeId);
}
