package jbst.iam.validators;

import jbst.iam.domain.dto.requests.RequestNewInvitationCodeParams;
import jbst.iam.domain.identifiers.InvitationCodeId;
import tech1.framework.foundation.domain.base.Username;

public interface BaseInvitationCodesRequestsValidator {
    void validateCreateNewInvitationCode(RequestNewInvitationCodeParams request);
    void validateDeleteById(Username username, InvitationCodeId invitationCodeId);
}
