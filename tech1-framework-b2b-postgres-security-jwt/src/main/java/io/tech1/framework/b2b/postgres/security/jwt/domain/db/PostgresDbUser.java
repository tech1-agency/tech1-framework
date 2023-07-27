package io.tech1.framework.b2b.postgres.security.jwt.domain.db;

import com.fasterxml.jackson.annotation.JsonIgnore;
import io.tech1.framework.b2b.postgres.security.jwt.converters.columns.*;
import io.tech1.framework.domain.base.Email;
import io.tech1.framework.domain.base.Password;
import io.tech1.framework.domain.base.Username;
import lombok.*;
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
    @GeneratedValue
    private Long id;

    @Convert(converter = PostgresUsernameConverter.class)
    @Column
    private Username username;

    @Convert(converter = PostgresPasswordConverter.class)
    @Column
    private Password password;

    @Convert(converter = PostgresZoneIdConverter.class)
    @Column(name = "zone_id")
    private ZoneId zoneId;

    @Convert(converter = PostgresSimpleGrantedAuthoritiesConverter.class)
    @Column
    private List<SimpleGrantedAuthority> authorities;

    @Convert(converter = PostgresEmailConverter.class)
    @Column
    private Email email;

    @Column
    private String name;

    @Convert(converter = PostgresMapStringsObjectsConverter.class)
    @Column
    private Map<String, Object> attributes;

    public PostgresDbUser(Username username, Password password, ZoneId zoneId, List<SimpleGrantedAuthority> authorities) {
        this.username = username;
        this.password = password;
        this.zoneId = zoneId;
        this.authorities = authorities;
    }

    @JsonIgnore
    public Map<String, Object> getNotNullAttributes() {
        return nonNull(this.attributes) ? this.attributes : new HashMap<>();
    }
}
