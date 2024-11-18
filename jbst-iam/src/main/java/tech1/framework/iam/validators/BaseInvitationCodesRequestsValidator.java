package tech1.framework.iam.validators;

import tech1.framework.iam.domain.dto.requests.RequestNewInvitationCodeParams;
import tech1.framework.iam.domain.identifiers.InvitationCodeId;
import tech1.framework.foundation.domain.base.Username;

public interface BaseInvitationCodesRequestsValidator {
    void validateCreateNewInvitationCode(RequestNewInvitationCodeParams request);
    void validateDeleteById(Username username, InvitationCodeId invitationCodeId);
}
