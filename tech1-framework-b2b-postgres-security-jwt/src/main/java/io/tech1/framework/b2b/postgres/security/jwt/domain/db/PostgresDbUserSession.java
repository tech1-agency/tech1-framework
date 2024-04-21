package io.tech1.framework.b2b.postgres.security.jwt.domain.db;

import com.fasterxml.jackson.annotation.JsonIgnore;
import io.tech1.framework.b2b.base.security.jwt.domain.db.UserSession;
import io.tech1.framework.b2b.base.security.jwt.domain.dto.responses.ResponseUserSession2;
import io.tech1.framework.b2b.base.security.jwt.domain.identifiers.UserSessionId;
import io.tech1.framework.b2b.base.security.jwt.domain.jwt.JwtAccessToken;
import io.tech1.framework.b2b.base.security.jwt.domain.jwt.JwtRefreshToken;
import io.tech1.framework.b2b.base.security.jwt.domain.jwt.RequestAccessToken;
import io.tech1.framework.b2b.postgres.security.jwt.converters.columns.PostgresJwtAccessTokenConverter;
import io.tech1.framework.b2b.postgres.security.jwt.converters.columns.PostgresJwtRefreshTokenConverter;
import io.tech1.framework.b2b.postgres.security.jwt.converters.columns.PostgresUserRequestMetadataConverter;
import io.tech1.framework.b2b.postgres.security.jwt.converters.columns.PostgresUsernameConverter;
import io.tech1.framework.b2b.postgres.security.jwt.domain.superclasses.PostgresDbAbstractPersistable1;
import io.tech1.framework.domain.base.Username;
import io.tech1.framework.domain.http.requests.UserRequestMetadata;
import lombok.*;

import javax.persistence.*;

import static io.tech1.framework.b2b.base.security.jwt.domain.db.UserSession.ofNotPersisted;
import static io.tech1.framework.b2b.base.security.jwt.domain.db.UserSession.ofPersisted;

@SuppressWarnings("JpaDataSourceORMInspection")
// Lombok
@NoArgsConstructor
@Getter
@Setter
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
// JPA
@Entity
@Table(name = PostgresDbUserSession.PG_TABLE_NAME)
public class PostgresDbUserSession extends PostgresDbAbstractPersistable1 {
    public static final String PG_TABLE_NAME = "tech1_users_sessions";

    @Convert(converter = PostgresUsernameConverter.class)
    @Column(nullable = false)
    private Username username;

    @Convert(converter = PostgresJwtAccessTokenConverter.class)
    @Column(name = "access_token", length = 1024, nullable = false)
    private JwtAccessToken accessToken;

    @Convert(converter = PostgresJwtRefreshTokenConverter.class)
    @Column(name = "refresh_token", length = 1024, nullable = false)
    private JwtRefreshToken refreshToken;

    @Convert(converter = PostgresUserRequestMetadataConverter.class)
    @Column(length = 65535, nullable = false)
    private UserRequestMetadata metadata;

    @Column(name = "metadata_renew_cron", nullable = false)
    private boolean metadataRenewCron;

    @Column(name = "metadata_renew_manually", nullable = false)
    private boolean metadataRenewManually;

    public PostgresDbUserSession(UserSession session) {
        if (session.persisted()) {
            this.id = session.id().value();
        }
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
        if (this.isNew()) {
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
