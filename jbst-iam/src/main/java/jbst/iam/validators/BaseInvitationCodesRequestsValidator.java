package jbst.iam.validators;

import jbst.iam.domain.dto.requests.RequestNewInvitationParams;
import jbst.iam.domain.identifiers.InvitationId;
import jbst.foundation.domain.base.Username;

public interface BaseInvitationCodesRequestsValidator {
    void validateCreateNewInvitationCode(RequestNewInvitationParams request);
    void validateDeleteById(Username username, InvitationId invitationId);
}
