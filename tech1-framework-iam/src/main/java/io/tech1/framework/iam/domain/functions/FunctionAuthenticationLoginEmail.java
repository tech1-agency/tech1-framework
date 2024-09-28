package io.tech1.framework.iam.domain.functions;

import tech1.framework.foundation.domain.base.Email;
import tech1.framework.foundation.domain.base.Username;
import tech1.framework.foundation.domain.http.requests.UserRequestMetadata;
import tech1.framework.foundation.domain.tuples.Tuple3;

public record FunctionAuthenticationLoginEmail(
        Username username,
        Email email,
        UserRequestMetadata requestMetadata
) {
    public Tuple3<Username, Email, UserRequestMetadata> getTuple3() {
        return new Tuple3<>(
                this.username,
                this.email,
                this.requestMetadata
        );
    }
}
