package jbst.iam.domain.functions;

import jbst.foundation.domain.base.Email;
import jbst.foundation.domain.base.Username;
import jbst.foundation.domain.http.requests.UserRequestMetadata;
import jbst.foundation.domain.tuples.Tuple3;

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
