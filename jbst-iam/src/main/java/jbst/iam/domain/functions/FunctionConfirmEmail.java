package jbst.iam.domain.functions;

import jbst.foundation.domain.base.Email;
import jbst.foundation.domain.base.Username;
import org.jetbrains.annotations.NotNull;

public record FunctionConfirmEmail(
        @NotNull Username username,
        @NotNull Email email,
        @NotNull String token
) {

    public static FunctionConfirmEmail hardcoded() {
        return new FunctionConfirmEmail(
                Username.hardcoded(),
                Email.hardcoded(),
                "V2orWAWX4xlvam9V7u5aUqpgriM6qd8qRsgGyqNw"
        );
    }

}
