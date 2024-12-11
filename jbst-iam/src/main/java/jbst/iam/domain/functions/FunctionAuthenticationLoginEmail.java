package jbst.iam.domain.functions;

import jbst.foundation.domain.base.Email;
import jbst.foundation.domain.base.Username;
import jbst.foundation.domain.http.requests.UserRequestMetadata;
import jbst.iam.domain.enums.AccountAccessMethod;
import org.jetbrains.annotations.NotNull;

public record FunctionAuthenticationLoginEmail(
        @NotNull Username username,
        @NotNull Email email,
        @NotNull UserRequestMetadata requestMetadata
) {

    public static FunctionAuthenticationLoginEmail hardcoded() {
        return new FunctionAuthenticationLoginEmail(Username.hardcoded(), Email.hardcoded(), UserRequestMetadata.valid());
    }

    public FunctionAccountAccessed getFunctionAccountAccessed() {
        return new FunctionAccountAccessed(this.username, this.email, this.requestMetadata, AccountAccessMethod.USERNAME_PASSWORD);
    }
}
