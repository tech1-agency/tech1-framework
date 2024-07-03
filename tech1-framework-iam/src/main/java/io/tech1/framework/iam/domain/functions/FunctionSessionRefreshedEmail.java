package io.tech1.framework.iam.domain.functions;

import io.tech1.framework.foundation.domain.base.Email;
import io.tech1.framework.foundation.domain.base.Username;
import io.tech1.framework.foundation.domain.http.requests.UserRequestMetadata;
import io.tech1.framework.foundation.domain.tuples.Tuple3;

public record FunctionSessionRefreshedEmail(
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
