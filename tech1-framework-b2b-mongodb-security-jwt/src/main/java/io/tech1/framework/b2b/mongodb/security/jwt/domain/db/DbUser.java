package io.tech1.framework.b2b.mongodb.security.jwt.domain.db;

import com.fasterxml.jackson.annotation.JsonIgnore;
import io.tech1.framework.b2b.mongodb.security.jwt.domain.dto.requests.RequestUserUpdate1;
import io.tech1.framework.b2b.mongodb.security.jwt.domain.dto.requests.RequestUserUpdate2;
import io.tech1.framework.domain.base.Email;
import io.tech1.framework.domain.base.Password;
import io.tech1.framework.domain.base.Username;
import lombok.*;
import org.springframework.data.annotation.Id;
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
public class DbUser {
    @Id
    private String id;

    private Username username;
    private List<SimpleGrantedAuthority> authorities;

    private Map<String, Object> attributes;

    private Password password;
    private ZoneId zoneId;

    private Email email;
    private String name;

    public DbUser(
            Username username,
            Password password,
            String zoneId,
            List<SimpleGrantedAuthority> authorities
    ) {
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

    public void edit1(RequestUserUpdate1 requestUserUpdate1) {
        this.zoneId = ZoneId.of(requestUserUpdate1.zoneId());
        this.email = requestUserUpdate1.email();
        this.name = requestUserUpdate1.name();
    }

    public void edit2(RequestUserUpdate2 requestUserUpdate2) {
        this.zoneId = ZoneId.of(requestUserUpdate2.zoneId());
        this.name = requestUserUpdate2.name();
    }

    public void changePassword(Password password) {
        this.password = password;
    }

    @JsonIgnore
    public Map<String, Object> getNotNullAttributes() {
        return nonNull(this.attributes) ? this.attributes : new HashMap<>();
    }
}
