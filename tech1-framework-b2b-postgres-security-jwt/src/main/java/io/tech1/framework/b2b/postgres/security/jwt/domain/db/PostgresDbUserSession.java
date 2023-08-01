package io.tech1.framework.b2b.postgres.security.jwt.domain.db;

import com.fasterxml.jackson.annotation.JsonIgnore;
import io.tech1.framework.b2b.base.security.jwt.domain.db.AnyDbUserSession;
import io.tech1.framework.b2b.base.security.jwt.domain.dto.responses.ResponseUserSession2;
import io.tech1.framework.b2b.base.security.jwt.domain.identifiers.UserSessionId;
import io.tech1.framework.b2b.base.security.jwt.domain.jwt.CookieAccessToken;
import io.tech1.framework.b2b.base.security.jwt.domain.jwt.JwtAccessToken;
import io.tech1.framework.b2b.base.security.jwt.domain.jwt.JwtRefreshToken;
import io.tech1.framework.b2b.postgres.security.jwt.converters.columns.PostgresJwtAccessTokenConverter;
import io.tech1.framework.b2b.postgres.security.jwt.converters.columns.PostgresJwtRefreshTokenConverter;
import io.tech1.framework.b2b.postgres.security.jwt.converters.columns.PostgresUserRequestMetadataConverter;
import io.tech1.framework.b2b.postgres.security.jwt.converters.columns.PostgresUsernameConverter;
import io.tech1.framework.domain.base.Username;
import io.tech1.framework.domain.http.requests.UserRequestMetadata;
import lombok.*;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;

import static io.tech1.framework.b2b.base.security.jwt.domain.db.AnyDbUserSession.ofNotPersisted;
import static io.tech1.framework.b2b.base.security.jwt.domain.db.AnyDbUserSession.ofPersisted;
import static io.tech1.framework.b2b.postgres.security.jwt.constants.PostgreTablesConstants.USERS_SESSIONS;
import static java.util.Objects.nonNull;

@SuppressWarnings("JpaDataSourceORMInspection")
// Lombok
@NoArgsConstructor
@Getter
@Setter
@EqualsAndHashCode
@ToString
// JPA
@Entity
@Table(name = USERS_SESSIONS)
public class PostgresDbUserSession {
    @Id
    @GenericGenerator(name = "uuid2", strategy = "uuid2")
    @GeneratedValue(strategy = GenerationType.IDENTITY, generator = "uuid2")
    @Column(length = 36, nullable = false, updatable = false)
    private String id;

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

    public PostgresDbUserSession(AnyDbUserSession session) {
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
    public AnyDbUserSession anyDbUserSession() {
        if (nonNull(this.id)) {
            return ofPersisted(new UserSessionId(this.id), this.username, this.accessToken, this.refreshToken, this.metadata);
        } else {
            return ofNotPersisted( this.username, this.accessToken, this.refreshToken, this.metadata);
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
