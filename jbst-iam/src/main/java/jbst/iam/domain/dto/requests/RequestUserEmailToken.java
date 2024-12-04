package jbst.iam.domain.dto.requests;

import jbst.foundation.domain.base.Email;
import jbst.foundation.domain.constants.JbstConstants;
import jbst.iam.domain.enums.UserEmailTokenType;

public record RequestUserEmailToken(
        Email email,
        UserEmailTokenType type
) {

    public static RequestUserEmailToken hardcoded() {
        return new RequestUserEmailToken(
                Email.of("user1@" + JbstConstants.Domains.HARDCODED),
                UserEmailTokenType.VERIFICATION
        );
    }

}
