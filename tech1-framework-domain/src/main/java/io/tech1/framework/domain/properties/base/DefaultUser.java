package io.tech1.framework.domain.properties.base;

import io.tech1.framework.domain.base.Password;
import io.tech1.framework.domain.base.Username;
import lombok.Data;

import java.time.ZoneId;
import java.util.List;

// Lombok (property-based)
@Data
public class DefaultUser {
    private String username;
    private String password;
    private ZoneId zoneId;
    private List<String> authorities;

    // NOTE: test-purposes
    public static DefaultUser of(
            String username,
            String password,
            ZoneId zoneId,
            List<String> authorities
    ) {
        var instance = new DefaultUser();
        instance.username = username;
        instance.password = password;
        instance.zoneId = zoneId;
        instance.authorities = authorities;
        return instance;
    }

    public Username getUsername() {
        return new Username(this.username);
    }

    public Password getPassword() {
        return new Password(this.password);
    }
}
