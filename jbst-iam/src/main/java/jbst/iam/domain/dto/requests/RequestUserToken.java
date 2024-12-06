package jbst.iam.domain.dto.requests;

import jbst.foundation.domain.base.Username;
import jbst.iam.domain.enums.UserTokenType;

public record RequestUserToken(
        Username username,
        UserTokenType type
) {

    public static RequestUserToken emailConfirmation(Username username) {
        return new RequestUserToken(
                username,
                UserTokenType.EMAIL_CONFIRMATION
        );
    }

    public static RequestUserToken hardcoded() {
        return new RequestUserToken(
                Username.hardcoded(),
                UserTokenType.EMAIL_CONFIRMATION
        );
    }

}
