package io.tech1.framework.b2b.base.security.jwt.domain.functions;

import io.tech1.framework.domain.base.Email;
import io.tech1.framework.domain.base.Username;
import io.tech1.framework.domain.http.requests.UserRequestMetadata;
import io.tech1.framework.domain.tuples.Tuple3;

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
