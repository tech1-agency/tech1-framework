package jbst.iam.domain.functions;

import jbst.foundation.domain.base.Email;
import jbst.foundation.domain.base.Username;
import jbst.foundation.domain.http.requests.UserRequestMetadata;
import org.jetbrains.annotations.NotNull;

public record FunctionAuthenticationLoginEmail(
        @NotNull Username username,
        @NotNull Email email,
        @NotNull UserRequestMetadata requestMetadata
) {

}
