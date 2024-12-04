package jbst.iam.domain.dto.requests;

import jbst.foundation.domain.base.Email;
import jbst.foundation.domain.constants.JbstConstants;
import jbst.iam.domain.enums.UserTokenType;

public record RequestUserToken(
        Email email,
        UserTokenType type
) {

    public static RequestUserToken hardcoded() {
        return new RequestUserToken(
                Email.of("user1@" + JbstConstants.Domains.HARDCODED),
                UserTokenType.EMAIL_CONFIRMATION
        );
    }

}
