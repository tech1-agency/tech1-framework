package io.tech1.framework.b2b.mongodb.security.jwt.validators;

import io.tech1.framework.b2b.mongodb.security.jwt.domain.db.DbUser;
import io.tech1.framework.b2b.mongodb.security.jwt.domain.dto.requests.RequestNewInvitationCodeParams;

public interface InvitationCodeRequestsValidator {
    void validateCreateNewInvitationCode(RequestNewInvitationCodeParams requestNewInvitationCodeParams);
    void validateDeleteById(DbUser currentUser, String invitationCodeId);
}
