package io.tech1.framework.b2b.mongodb.security.jwt.domain.db;

import io.tech1.framework.b2b.mongodb.security.jwt.domain.jwt.JwtRefreshToken;
import io.tech1.framework.domain.base.Username;
import io.tech1.framework.domain.http.requests.UserRequestMetadata;
import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

// Lombok
@NoArgsConstructor
@Getter
@Setter
@EqualsAndHashCode
@ToString
// Mongodb
@Document(collection = "tech1_users_sessions")
public class DbUserSession {
    @Id
    private String id;
    private Username username;

    private UserRequestMetadata requestMetadata;

    public DbUserSession(
            JwtRefreshToken jwtRefreshToken,
            Username username,
            UserRequestMetadata requestMetadata
    ) {
        this.id = jwtRefreshToken.getValue();
        this.username = username;
        this.requestMetadata = requestMetadata;
    }

    public JwtRefreshToken getJwtRefreshToken() {
        return new JwtRefreshToken(this.id);
    }

    public void editRequestMetadata(UserRequestMetadata requestMetadata) {
        this.requestMetadata = requestMetadata;
    }
}
