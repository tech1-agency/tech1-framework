package io.tech1.framework.b2b.postgres.security.jwt.domain.db;

import com.fasterxml.jackson.annotation.JsonIgnore;
import io.tech1.framework.b2b.base.security.jwt.domain.dto.responses.ResponseUserSession2;
import io.tech1.framework.b2b.base.security.jwt.domain.identifiers.UserSessionId;
import io.tech1.framework.b2b.base.security.jwt.domain.jwt.CookieRefreshToken;
import io.tech1.framework.b2b.base.security.jwt.domain.jwt.JwtRefreshToken;
import io.tech1.framework.b2b.postgres.security.jwt.converters.columns.PostgresUserRequestMetadataConverter;
import io.tech1.framework.b2b.postgres.security.jwt.converters.columns.PostgresUsernameConverter;
import io.tech1.framework.domain.base.Username;
import io.tech1.framework.domain.http.requests.UserRequestMetadata;
import lombok.*;

import javax.persistence.*;

import static io.tech1.framework.b2b.postgres.security.jwt.constants.PostgreTablesConstants.USERS_SESSIONS;

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
//    @GenericGenerator(name = "uuid2", strategy = "uuid2")
//    @GeneratedValue(strategy = GenerationType.IDENTITY, generator = "uuid2")
//    @Column(length = 36, nullable = false, updatable = false)
    @Column(length = 256, nullable = false, updatable = false)
    private String id;

    @Convert(converter = PostgresUsernameConverter.class)
    @Column(nullable = false)
    private Username username;

    @Convert(converter = PostgresUserRequestMetadataConverter.class)
    @Column(length = 65535, nullable = false)
    private UserRequestMetadata metadata;

    public PostgresDbUserSession(
            JwtRefreshToken jwtRefreshToken,
            Username username,
            UserRequestMetadata metadata
    ) {
        this.id = jwtRefreshToken.value();
        this.username = username;
        this.metadata = metadata;
    }

    @Deprecated
    @JsonIgnore
    @Transient
    public JwtRefreshToken getJwtRefreshToken() {
        return new JwtRefreshToken(this.id);
    }

    @JsonIgnore
    @Transient
    public ResponseUserSession2 responseUserSession2(CookieRefreshToken cookie) {
        return ResponseUserSession2.of(
                this.username,
                this.metadata,
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
