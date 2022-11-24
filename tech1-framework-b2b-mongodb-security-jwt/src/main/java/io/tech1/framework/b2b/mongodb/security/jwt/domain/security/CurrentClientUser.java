package io.tech1.framework.b2b.mongodb.security.jwt.domain.security;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.tech1.framework.domain.base.AbstractAuthority;
import io.tech1.framework.domain.base.Username;
import lombok.*;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.time.ZoneId;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static java.util.Objects.nonNull;

// Lombok
@Data
public class CurrentClientUser {
    @JsonIgnore
    private final String id;
    private final Username username;
    private final String email;
    private final String name;
    private final ZoneId zoneId;
    private final List<String> authorities;
    private Map<String, Object> attributes;

    public CurrentClientUser(
            String id,
            Username username,
            String email,
            String name,
            ZoneId zoneId,
            List<SimpleGrantedAuthority> authorities,
            Map<String, Object> attributes
    ) {
        this.id = id;
        this.username = username;
        this.email = email;
        this.name = name;
        this.zoneId = zoneId;
        this.authorities = authorities.stream().map(SimpleGrantedAuthority::getAuthority).collect(Collectors.toList());
        this.attributes = attributes;
    }

    @JsonIgnore
    public Object getAttributeByKey(String key) {
        if (nonNull(this.attributes)) {
            return this.attributes.get(key);
        } else {
            return null;
        }
    }

    public boolean hasAuthority(AbstractAuthority authority) {
        return this.authorities.contains(authority.getValue());
    }

    public boolean hasAuthority(String authority) {
        return this.authorities.contains(authority);
    }
}
