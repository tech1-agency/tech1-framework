package io.tech1.framework.b2b.postgres.security.jwt.domain.db;

import com.fasterxml.jackson.annotation.JsonIgnore;
import io.tech1.framework.b2b.base.security.jwt.domain.identifiers.UserId;
import io.tech1.framework.b2b.base.security.jwt.domain.jwt.JwtUser;
import io.tech1.framework.b2b.postgres.security.jwt.converters.columns.*;
import io.tech1.framework.b2b.postgres.security.jwt.domain.superclasses.PostgresDbAbstractPersistable0;
import io.tech1.framework.domain.base.Email;
import io.tech1.framework.domain.base.Password;
import io.tech1.framework.domain.base.Username;
import lombok.*;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import jakarta.persistence.*;
import java.time.ZoneId;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import static java.util.Objects.nonNull;

@SuppressWarnings("JpaDataSourceORMInspection")
// Lombok
@NoArgsConstructor
@Getter
@Setter
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
// JPA
@Entity
@Table(name = PostgresDbUser.PG_TABLE_NAME)
public class PostgresDbUser extends PostgresDbAbstractPersistable0 {
    public static final String PG_TABLE_NAME = "tech1_users";

    @Convert(converter = PostgresUsernameConverter.class)
    @Column(nullable = false, updatable = false)
    private Username username;

    @Convert(converter = PostgresPasswordConverter.class)
    @Column(nullable = false)
    private Password password;

    @Convert(converter = PostgresZoneIdConverter.class)
    @Column(name = "zone_id", nullable = false)
    private ZoneId zoneId;

    @Convert(converter = PostgresSetOfSimpleGrantedAuthoritiesConverter.class)
    @Column(length = 1024, nullable = false)
    private Set<SimpleGrantedAuthority> authorities;

    @Convert(converter = PostgresEmailConverter.class)
    @Column
    private Email email;

    @Column
    private String name;

    @Column(name = "password_change_required", nullable = false)
    private boolean passwordChangeRequired;

    @Convert(converter = PostgresMapStringsObjectsConverter.class)
    @Column(length = 65535)
    private Map<String, Object> attributes;

    public PostgresDbUser(Username username, Password password, ZoneId zoneId, Set<SimpleGrantedAuthority> authorities, boolean passwordChangeRequired) {
        this.username = username;
        this.password = password;
        this.zoneId = zoneId;
        this.authorities = authorities;
        this.passwordChangeRequired = passwordChangeRequired;
    }

    public PostgresDbUser(JwtUser user) {
        this.id = user.id().value();
        this.username = user.username();
        this.password = user.password();
        this.zoneId = user.zoneId();
        this.authorities = user.authorities();
        this.email = user.email();
        this.name = user.name();
        this.passwordChangeRequired = user.passwordChangeRequired();
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
                this.passwordChangeRequired,
                this.attributes
        );
    }
}
