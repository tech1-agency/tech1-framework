package jbst.iam.validators;

import jbst.iam.domain.dto.requests.RequestNewInvitationParams;
import jbst.iam.domain.identifiers.InvitationId;
import jbst.foundation.domain.base.Username;

public interface BaseInvitationsRequestsValidator {
    void validateCreateNewInvitation(RequestNewInvitationParams request);
    void validateDeleteById(Username username, InvitationId invitationId);
}
