package io.tech1.framework.b2b.postgres.server.domain;

import io.tech1.framework.domain.base.Email;
import io.tech1.framework.domain.base.Username;
import lombok.Data;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.time.ZoneId;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

// WARNING: delete and reuse mongodb-based (migrate -> base-security-jwt)
// Lombok
@Data
public class CurrentClientUser {
    private final Username username;
    private final Email email;
    private final String name;
    private final ZoneId zoneId;
    private final List<String> authorities;
    private Map<String, Object> attributes;

    public CurrentClientUser(
            Username username,
            Email email,
            String name,
            ZoneId zoneId,
            List<SimpleGrantedAuthority> authorities,
            Map<String, Object> attributes
    ) {
        this.username = username;
        this.email = email;
        this.name = name;
        this.zoneId = zoneId;
        this.authorities = authorities.stream().map(SimpleGrantedAuthority::getAuthority).collect(Collectors.toList());
        this.attributes = attributes;
    }

}
