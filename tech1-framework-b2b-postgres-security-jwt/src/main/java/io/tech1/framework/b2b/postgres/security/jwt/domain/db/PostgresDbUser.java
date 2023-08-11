package io.tech1.framework.b2b.postgres.security.jwt.domain.db;

import com.fasterxml.jackson.annotation.JsonIgnore;
import io.tech1.framework.b2b.base.security.jwt.domain.identifiers.UserId;
import io.tech1.framework.b2b.base.security.jwt.domain.jwt.JwtUser;
import io.tech1.framework.b2b.postgres.security.jwt.converters.columns.*;
import io.tech1.framework.domain.base.Email;
import io.tech1.framework.domain.base.Password;
import io.tech1.framework.domain.base.Username;
import lombok.*;
import org.hibernate.annotations.GenericGenerator;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import javax.persistence.*;
import java.time.ZoneId;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static io.tech1.framework.b2b.postgres.security.jwt.constants.PostgreTablesConstants.USERS;
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
@Table(name = USERS)
public class PostgresDbUser {
    @Id
    @GenericGenerator(name = "uuid2", strategy = "uuid2")
    @GeneratedValue(strategy = GenerationType.IDENTITY, generator = "uuid2")
    @Column(length = 36, nullable = false, updatable = false)
    private String id;

    @Convert(converter = PostgresUsernameConverter.class)
    @Column(nullable = false, updatable = false)
    private Username username;

    @Convert(converter = PostgresPasswordConverter.class)
    @Column(nullable = false)
    private Password password;

    @Convert(converter = PostgresZoneIdConverter.class)
    @Column(name = "zone_id", nullable = false)
    private ZoneId zoneId;

    @Convert(converter = PostgresSimpleGrantedAuthoritiesConverter.class)
    @Column(length = 1024, nullable = false)
    private List<SimpleGrantedAuthority> authorities;

    @Convert(converter = PostgresEmailConverter.class)
    @Column
    private Email email;

    @Column
    private String name;

    @Convert(converter = PostgresMapStringsObjectsConverter.class)
    @Column(length = 65535)
    private Map<String, Object> attributes;

    public PostgresDbUser(Username username, Password password, ZoneId zoneId, List<SimpleGrantedAuthority> authorities) {
        this.username = username;
        this.password = password;
        this.zoneId = zoneId;
        this.authorities = authorities;
    }

    public PostgresDbUser(JwtUser user) {
        this.id = user.id().value();
        this.username = user.username();
        this.password = user.password();
        this.zoneId = user.zoneId();
        this.authorities = user.authorities();
        this.email = user.email();
        this.name = user.name();
        this.attributes = user.attributes();
    }

    @JsonIgnore
    public Map<String, Object> getNotNullAttributes() {
        return nonNull(this.attributes) ? this.attributes : new HashMap<>();
    }

    @JsonIgnore
    @Transient
    public UserId userId() {
        return new UserId(this.id);
    }

    @JsonIgnore
    @Transient
    public JwtUser asJwtUser() {
        return new JwtUser(
                this.userId(),
                this.username,
                this.password,
                this.zoneId,
                this.authorities,
                this.email,
                this.name,
                this.attributes
        );
    }
}
