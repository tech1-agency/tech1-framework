package io.tech1.framework.domain.properties.base;

import io.tech1.framework.domain.base.Email;
import io.tech1.framework.domain.base.Password;
import io.tech1.framework.domain.base.Username;
import io.tech1.framework.domain.properties.annotations.MandatoryProperty;
import io.tech1.framework.domain.properties.annotations.NonMandatoryProperty;
import io.tech1.framework.domain.properties.configs.AbstractPropertiesConfigs;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.boot.context.properties.ConstructorBinding;

import java.time.ZoneId;
import java.util.List;

import static java.util.Objects.nonNull;

// Lombok (property-based)
@AllArgsConstructor(onConstructor = @__({@ConstructorBinding}))
@Data
@EqualsAndHashCode(callSuper = true)
public class DefaultUser extends AbstractPropertiesConfigs {
    @MandatoryProperty
    private final String username;
    @MandatoryProperty
    private final String password;
    @MandatoryProperty
    private final ZoneId zoneId;
    @NonMandatoryProperty
    private String email;
    @MandatoryProperty
    private final List<String> authorities;

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
