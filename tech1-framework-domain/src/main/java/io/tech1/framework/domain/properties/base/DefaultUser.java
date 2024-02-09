package io.tech1.framework.domain.properties.base;

import io.tech1.framework.domain.base.Email;
import io.tech1.framework.domain.base.Password;
import io.tech1.framework.domain.base.Username;
import io.tech1.framework.domain.properties.annotations.MandatoryProperty;
import io.tech1.framework.domain.properties.annotations.NonMandatoryProperty;
import io.tech1.framework.domain.utilities.random.RandomUtility;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.boot.context.properties.ConstructorBinding;

import java.time.ZoneId;
import java.util.Set;

import static io.tech1.framework.domain.utilities.random.RandomUtility.randomStringsAsSet;
import static java.util.Objects.nonNull;

// Lombok (property-based)
@AllArgsConstructor(onConstructor = @__({@ConstructorBinding}))
@Data
@EqualsAndHashCode(callSuper = true)
public class DefaultUser extends AbstractPropertyConfigs {
    @MandatoryProperty
    private final Username username;
    @MandatoryProperty
    private final Password password;
    @MandatoryProperty
    private final ZoneId zoneId;
    @NonMandatoryProperty
    private String email;
    @MandatoryProperty
    private final Set<String> authorities;

    public static DefaultUser testsHardcoded() {
        return new DefaultUser(
                Username.testsHardcoded(),
                Password.testsHardcoded(),
                ZoneId.of("Europe/Kiev"),
                Email.testsHardcoded().value(),
                Set.of("user", "admin")
        );
    }

    public static DefaultUser random() {
        return new DefaultUser(
                Username.random(),
                Password.random(),
                RandomUtility.randomZoneId(),
                Email.random().value(),
                randomStringsAsSet(3)
        );
    }

    public Email getEmailOrNull() {
        return nonNull(this.email) ? Email.of(this.email) : null;
    }
}
