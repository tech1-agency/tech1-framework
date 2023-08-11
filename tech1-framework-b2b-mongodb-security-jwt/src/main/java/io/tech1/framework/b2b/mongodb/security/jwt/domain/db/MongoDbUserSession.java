package io.tech1.framework.b2b.mongodb.security.jwt.domain.db;

import com.fasterxml.jackson.annotation.JsonIgnore;
import io.tech1.framework.b2b.base.security.jwt.domain.db.UserSession;
import io.tech1.framework.b2b.base.security.jwt.domain.dto.responses.ResponseUserSession2;
import io.tech1.framework.b2b.base.security.jwt.domain.identifiers.UserSessionId;
import io.tech1.framework.b2b.base.security.jwt.domain.jwt.CookieAccessToken;
import io.tech1.framework.b2b.base.security.jwt.domain.jwt.JwtAccessToken;
import io.tech1.framework.b2b.base.security.jwt.domain.jwt.JwtRefreshToken;
import io.tech1.framework.domain.base.Username;
import io.tech1.framework.domain.http.requests.UserRequestMetadata;
import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Transient;
import org.springframework.data.mongodb.core.mapping.Document;

import static io.tech1.framework.b2b.base.security.jwt.domain.db.UserSession.ofNotPersisted;
import static io.tech1.framework.b2b.base.security.jwt.domain.db.UserSession.ofPersisted;
import static java.util.Objects.nonNull;

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
    private JwtAccessToken accessToken;
    private JwtRefreshToken refreshToken;
    private UserRequestMetadata metadata;

    public MongoDbUserSession(UserSession session) {
        if (session.persisted()) {
            this.id = session.id().value();
        }
        this.username = session.username();
        this.accessToken = session.accessToken();
        this.refreshToken = session.refreshToken();
        this.metadata = session.metadata();
    }

    @JsonIgnore
    @Transient
    public UserSession userSession() {
        if (nonNull(this.id)) {
            return ofPersisted(new UserSessionId(this.id), this.username, this.accessToken, this.refreshToken, this.metadata);
        } else {
            return ofNotPersisted(this.username, this.accessToken, this.refreshToken, this.metadata);
        }
    }

    @JsonIgnore
    @Transient
    public ResponseUserSession2 responseUserSession2(CookieAccessToken cookie) {
        return ResponseUserSession2.of(new UserSessionId(this.id), this.username, cookie, this.accessToken, this.metadata);
    }

    @JsonIgnore
    @Transient
    public UserSessionId userSessionId() {
        return new UserSessionId(this.id);
    }
}
