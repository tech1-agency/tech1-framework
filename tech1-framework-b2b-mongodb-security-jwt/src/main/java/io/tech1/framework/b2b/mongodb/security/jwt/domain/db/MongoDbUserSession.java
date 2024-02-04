package io.tech1.framework.b2b.mongodb.security.jwt.domain.db;

import com.fasterxml.jackson.annotation.JsonIgnore;
import io.tech1.framework.b2b.base.security.jwt.domain.db.UserSession;
import io.tech1.framework.b2b.base.security.jwt.domain.dto.responses.ResponseUserSession2;
import io.tech1.framework.b2b.base.security.jwt.domain.identifiers.UserSessionId;
import io.tech1.framework.b2b.base.security.jwt.domain.jwt.RequestAccessToken;
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
import static io.tech1.framework.domain.utilities.time.TimestampUtility.getCurrentTimestamp;
import static java.util.Objects.isNull;

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
    private long createdAt;
    private long updatedAt;
    private Username username;
    private JwtAccessToken accessToken;
    private JwtRefreshToken refreshToken;
    private UserRequestMetadata metadata;
    private boolean metadataRenewCron;
    private boolean metadataRenewManually;

    public MongoDbUserSession(UserSession session) {
        if (session.persisted()) {
            this.id = session.id().value();
        }
        var currentTimestamp = getCurrentTimestamp();
        this.createdAt = currentTimestamp;
        this.updatedAt = currentTimestamp;
        this.username = session.username();
        this.accessToken = session.accessToken();
        this.refreshToken = session.refreshToken();
        this.metadata = session.metadata();
        this.metadataRenewCron = false;
        this.metadataRenewManually = false;
    }

    @JsonIgnore
    @Transient
    public UserSessionId userSessionId() {
        return new UserSessionId(this.id);
    }

    @JsonIgnore
    @Transient
    public UserSession userSession() {
        if (isNull(this.id)) {
            return ofNotPersisted(this.username, this.accessToken, this.refreshToken, this.metadata);
        } else {
            return ofPersisted(
                    this.userSessionId(),
                    this.createdAt,
                    this.updatedAt,
                    this.username,
                    this.accessToken,
                    this.refreshToken,
                    this.metadata,
                    this.metadataRenewCron,
                    this.metadataRenewManually
            );
        }
    }

    @JsonIgnore
    @Transient
    public ResponseUserSession2 responseUserSession2(RequestAccessToken requestAccessToken) {
        return ResponseUserSession2.of(
                this.userSessionId(),
                this.updatedAt,
                this.username,
                requestAccessToken,
                this.accessToken,
                this.metadata
        );
    }
}
