package io.tech1.framework.b2b.mongodb.security.jwt.domain.db;

import com.fasterxml.jackson.annotation.JsonIgnore;
import io.tech1.framework.b2b.base.security.jwt.domain.identifiers.UserId;
import io.tech1.framework.b2b.base.security.jwt.domain.jwt.JwtUser;
import io.tech1.framework.domain.base.Email;
import io.tech1.framework.domain.base.Password;
import io.tech1.framework.domain.base.Username;
import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Transient;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.time.ZoneId;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static io.tech1.framework.domain.asserts.Asserts.assertNonNullOrThrow;
import static io.tech1.framework.domain.asserts.Asserts.assertZoneIdOrThrow;
import static io.tech1.framework.domain.utilities.exceptions.ExceptionsMessagesUtility.invalidAttribute;
import static java.util.Objects.nonNull;

// Lombok
@NoArgsConstructor
@Getter
@Setter
@EqualsAndHashCode
@ToString
// Mongodb
@Document(collection = "tech1_users")
public class MongoDbUser {
    @Id
    private String id;

    private Username username;
    private List<SimpleGrantedAuthority> authorities;

    private Map<String, Object> attributes;

    private Password password;
    private ZoneId zoneId;

    private Email email;
    private String name;

    public MongoDbUser(Username username, Password password, String zoneId, List<SimpleGrantedAuthority> authorities) {
        assertNonNullOrThrow(username, invalidAttribute("DbUser.username"));
        assertNonNullOrThrow(password, invalidAttribute("DbUser.password"));
        assertZoneIdOrThrow(zoneId, invalidAttribute("DbUser.zoneId"));
        assertNonNullOrThrow(authorities, invalidAttribute("DbUser.authorities"));
        this.username = username;
        this.password = password;
        this.zoneId = ZoneId.of(zoneId);
        this.authorities = authorities;
        this.attributes = new HashMap<>();
    }

    public MongoDbUser(JwtUser user) {
        this.id = user.id().value();
        // TODO
    }

    @JsonIgnore
    @Transient
    public Map<String, Object> getNotNullAttributes() {
        return nonNull(this.attributes) ? this.attributes : new HashMap<>();
    }

    @JsonIgnore
    @Transient
    public JwtUser asJwtUser() {
        return new JwtUser(
                new UserId(this.id),
                this.username,
                this.password,
                this.authorities,
                this.email,
                this.name,
                this.zoneId,
                this.attributes
        );
    }
}
