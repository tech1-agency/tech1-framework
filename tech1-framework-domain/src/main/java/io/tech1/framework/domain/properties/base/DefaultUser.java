package io.tech1.framework.domain.properties.base;

import io.tech1.framework.domain.base.Email;
import io.tech1.framework.domain.base.Password;
import io.tech1.framework.domain.base.Username;
import io.tech1.framework.domain.properties.annotations.MandatoryProperty;
import io.tech1.framework.domain.properties.annotations.NonMandatoryProperty;
import lombok.Data;

import java.time.ZoneId;
import java.util.List;

import static java.util.Objects.nonNull;

// Lombok (property-based)
@Data
public class DefaultUser {
    @MandatoryProperty
    private String username;
    @MandatoryProperty
    private String password;
    @MandatoryProperty
    private ZoneId zoneId;
    @NonMandatoryProperty
    private String email;
    @MandatoryProperty
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
        return Username.of(this.username);
    }

    public Password getPassword() {
        return Password.of(this.password);
    }

    public Email getEmail() {
        return nonNull(this.email) ? Email.of(this.email) : null;
    }
}
