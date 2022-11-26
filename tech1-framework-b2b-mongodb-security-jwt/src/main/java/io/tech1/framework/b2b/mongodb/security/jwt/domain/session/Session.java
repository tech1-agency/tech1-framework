package io.tech1.framework.b2b.mongodb.security.jwt.domain.session;

import io.tech1.framework.b2b.mongodb.security.jwt.domain.jwt.JwtRefreshToken;
import io.tech1.framework.domain.base.Username;
import lombok.Data;

// Lombok
@Data(staticConstructor = "of")
public class Session {
    private final Username username;
    private final JwtRefreshToken refreshToken;
}

