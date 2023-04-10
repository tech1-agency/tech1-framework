package io.tech1.framework.b2b.mongodb.security.jwt.domain.functions;

import io.tech1.framework.domain.base.Email;
import io.tech1.framework.domain.base.Username;
import io.tech1.framework.domain.http.requests.UserRequestMetadata;
import io.tech1.framework.domain.tuples.Tuple3;
import lombok.Data;

// Lombok
@Data
public class FunctionSessionRefreshedEmail {
    private final Username username;
    private final Email email;
    private final UserRequestMetadata requestMetadata;

    public Tuple3<Username, Email, UserRequestMetadata> getTuple3() {
        return new Tuple3<>(
                this.username,
                this.email,
                this.requestMetadata
        );
    }
}
