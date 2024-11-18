package jbst.iam.validators;

import jbst.iam.domain.dto.requests.RequestNewInvitationCodeParams;
import jbst.iam.domain.identifiers.InvitationCodeId;
import jbst.foundation.domain.base.Username;

public interface BaseInvitationCodesRequestsValidator {
    void validateCreateNewInvitationCode(RequestNewInvitationCodeParams request);
    void validateDeleteById(Username username, InvitationCodeId invitationCodeId);
}
