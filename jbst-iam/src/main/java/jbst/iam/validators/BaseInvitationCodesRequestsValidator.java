package jbst.iam.validators;

import jbst.iam.domain.dto.requests.RequestNewInvitationCodeParams;
import jbst.iam.domain.identifiers.InvitationId;
import jbst.foundation.domain.base.Username;

public interface BaseInvitationCodesRequestsValidator {
    void validateCreateNewInvitationCode(RequestNewInvitationCodeParams request);
    void validateDeleteById(Username username, InvitationId invitationId);
}
