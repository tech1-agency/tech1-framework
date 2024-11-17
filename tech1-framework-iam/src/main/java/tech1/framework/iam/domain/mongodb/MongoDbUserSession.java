package tech1.framework.iam.domain.mongodb;

import com.fasterxml.jackson.annotation.JsonIgnore;
import tech1.framework.iam.domain.db.UserSession;
import tech1.framework.iam.domain.dto.responses.ResponseUserSession2;
import tech1.framework.iam.domain.identifiers.UserSessionId;
import tech1.framework.iam.domain.jwt.JwtAccessToken;
import tech1.framework.iam.domain.jwt.JwtRefreshToken;
import tech1.framework.iam.domain.jwt.RequestAccessToken;
import tech1.framework.foundation.domain.base.Username;
import tech1.framework.foundation.domain.http.requests.UserRequestMetadata;
import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Transient;
import org.springframework.data.mongodb.core.mapping.Document;

import static tech1.framework.iam.domain.db.UserSession.ofNotPersisted;
import static tech1.framework.iam.domain.db.UserSession.ofPersisted;
import static tech1.framework.foundation.utilities.time.TimestampUtility.getCurrentTimestamp;
import static java.util.Objects.isNull;

// Lombok
@NoArgsConstructor
@Getter
@Setter
@EqualsAndHashCode
@ToString
// Mongodb
@Document(collection = MongoDbUserSession.MONGO_TABLE_NAME)
public class MongoDbUserSession {
    public static final String MONGO_TABLE_NAME = "tech1_users_sessions";

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

    public static MongoDbUserSession random(String owner) {
        return new MongoDbUserSession(UserSession.random(owner));
    }

    public static MongoDbUserSession random(Username owner, String accessToken) {
        return new MongoDbUserSession(UserSession.random(owner, accessToken));
    }

    public static MongoDbUserSession random(String owner, String accessToken, String refreshToken) {
        return new MongoDbUserSession(UserSession.random(owner, accessToken, refreshToken));
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
