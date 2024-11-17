package tech1.framework.iam.domain.postgres.db;

import com.fasterxml.jackson.annotation.JsonIgnore;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import tech1.framework.foundation.domain.base.Email;
import tech1.framework.foundation.domain.base.Password;
import tech1.framework.foundation.domain.base.Username;
import tech1.framework.foundation.domain.constants.JbsConstants;
import tech1.framework.foundation.domain.converters.columns.*;
import tech1.framework.iam.converters.columns.PostgresSetOfSimpleGrantedAuthoritiesConverter;
import tech1.framework.iam.domain.identifiers.UserId;
import tech1.framework.iam.domain.jwt.JwtUser;
import tech1.framework.iam.domain.postgres.superclasses.PostgresDbAbstractPersistable0;

import java.time.ZoneId;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import static java.util.Objects.nonNull;
import static org.springframework.util.StringUtils.capitalize;
import static tech1.framework.foundation.domain.base.AbstractAuthority.*;
import static tech1.framework.foundation.utilities.random.RandomUtility.*;
import static tech1.framework.iam.utilities.SpringAuthoritiesUtility.getSimpleGrantedAuthorities;

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

    @Schema(type = "string")
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

    public static PostgresDbUser random(String username, Set<String> authorities) {
        var user = new PostgresDbUser(
                Username.of(username),
                Password.random(),
                randomZoneId(),
                getSimpleGrantedAuthorities(authorities),
                randomBoolean()
        );
        user.setEmail(Email.of(username + "@" + JbsConstants.Domains.HARDCODED));
        user.setName(capitalize(randomString()) + " " + capitalize(randomString()));
        user.setAttributes(
                Map.of(
                        randomString(), randomString(),
                        randomString(), randomLong()
                )
        );
        return user;
    }

    public static PostgresDbUser random(String username, String authority) {
        return random(username, Set.of(authority));
    }

    public static PostgresDbUser randomSuperadmin(String username) {
        return random(username, SUPERADMIN);
    }

    public static PostgresDbUser randomAdmin(String username) {
        return random(username, "admin");
    }

    public static List<PostgresDbUser> dummies1() {
        return List.of(
                PostgresDbUser.randomSuperadmin("sa1"),
                PostgresDbUser.randomSuperadmin("sa2"),
                PostgresDbUser.randomAdmin("admin1"),
                PostgresDbUser.random("user1", Set.of("user", INVITATION_CODE_WRITE)),
                PostgresDbUser.random("user2", Set.of("user", INVITATION_CODE_READ)),
                PostgresDbUser.random("sa3", Set.of(INVITATION_CODE_READ, SUPERADMIN, INVITATION_CODE_WRITE))
        );
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
