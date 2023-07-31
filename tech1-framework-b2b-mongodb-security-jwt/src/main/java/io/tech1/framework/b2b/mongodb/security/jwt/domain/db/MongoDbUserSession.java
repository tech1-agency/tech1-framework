package io.tech1.framework.b2b.mongodb.security.jwt.domain.db;

import com.fasterxml.jackson.annotation.JsonIgnore;
import io.tech1.framework.b2b.base.security.jwt.domain.db.AnyDbUserSession;
import io.tech1.framework.b2b.base.security.jwt.domain.dto.responses.ResponseUserSession2;
import io.tech1.framework.b2b.base.security.jwt.domain.identifiers.UserSessionId;
import io.tech1.framework.b2b.base.security.jwt.domain.jwt.CookieRefreshToken;
import io.tech1.framework.b2b.base.security.jwt.domain.jwt.JwtRefreshToken;
import io.tech1.framework.domain.base.Username;
import io.tech1.framework.domain.http.requests.UserRequestMetadata;
import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Transient;
import org.springframework.data.mongodb.core.mapping.Document;

// Lombok
@NoArgsConstructor
@Getter
@Setter
@EqualsAndHashCode
@ToString
// Mongodb
@Document(collection = "tech1_users_sessions")
public class MongoDbUserSession {
    @Id
    private String id;
    private Username username;

    // TODO [YY] rename -> metadata
    private UserRequestMetadata requestMetadata;

    public MongoDbUserSession(JwtRefreshToken jwtRefreshToken, Username username, UserRequestMetadata requestMetadata) {
        this.id = jwtRefreshToken.value();
        this.username = username;
        this.requestMetadata = requestMetadata;
    }

    public MongoDbUserSession(AnyDbUserSession session) {
        this.id = session.id().value();
        this.username = session.username();
        this.requestMetadata = session.metadata();
    }

    @Deprecated
    @JsonIgnore
    @Transient
    public JwtRefreshToken getJwtRefreshToken() {
        return new JwtRefreshToken(this.id);
    }

    @JsonIgnore
    @Transient
    public AnyDbUserSession anyDbUserSession() {
        return new AnyDbUserSession(
                new UserSessionId(this.id),
                this.username,
                this.requestMetadata,
                this.getJwtRefreshToken()
        );
    }

    @JsonIgnore
    @Transient
    public ResponseUserSession2 responseUserSession2(CookieRefreshToken cookie) {
        return ResponseUserSession2.of(
                this.username,
                this.requestMetadata,
                this.getJwtRefreshToken(),
                cookie
        );
    }

    @JsonIgnore
    @Transient
    public UserSessionId userSessionId() {
        return new UserSessionId(this.id);
    }
}
