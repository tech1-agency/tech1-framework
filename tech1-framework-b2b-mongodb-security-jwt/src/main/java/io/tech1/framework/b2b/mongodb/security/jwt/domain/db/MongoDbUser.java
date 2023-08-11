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
    private Password password;
    private ZoneId zoneId;
    private List<SimpleGrantedAuthority> authorities;
    private Email email;
    private String name;
    private Map<String, Object> attributes;

    public MongoDbUser(Username username, Password password, String zoneId, List<SimpleGrantedAuthority> authorities) {
        this.username = username;
        this.password = password;
        this.zoneId = ZoneId.of(zoneId);
        this.authorities = authorities;
        this.attributes = new HashMap<>();
    }

    public MongoDbUser(JwtUser user) {
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
    @Transient
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
