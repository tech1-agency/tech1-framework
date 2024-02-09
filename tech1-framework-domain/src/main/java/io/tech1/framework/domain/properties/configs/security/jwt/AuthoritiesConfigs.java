package io.tech1.framework.domain.properties.configs.security.jwt;

import io.tech1.framework.domain.properties.annotations.MandatoryProperty;
import io.tech1.framework.domain.properties.base.AbstractPropertyConfigs;
import io.tech1.framework.domain.properties.base.Authority;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.boot.context.properties.ConstructorBinding;

import java.util.Set;
import java.util.stream.Collectors;

import static io.tech1.framework.domain.base.AbstractAuthority.*;
import static io.tech1.framework.domain.utilities.random.RandomUtility.randomString;
import static io.tech1.framework.domain.utilities.random.RandomUtility.randomStringsAsSet;

// Lombok (property-based)
@AllArgsConstructor(onConstructor = @__({@ConstructorBinding}))
@Data
@EqualsAndHashCode(callSuper = true)
public class AuthoritiesConfigs extends AbstractPropertyConfigs {
    @MandatoryProperty
    private final String packageName;
    @MandatoryProperty
    private final Set<Authority> authorities;

    public static AuthoritiesConfigs testsHardcoded() {
        return new AuthoritiesConfigs(
                "io.tech1",
                Set.of(
                        new Authority(SUPER_ADMIN),
                        new Authority("admin"),
                        new Authority("user"),
                        new Authority(INVITATION_CODE_READ),
                        new Authority(INVITATION_CODE_WRITE)
                )
        );
    }

    public static AuthoritiesConfigs random() {
        return new AuthoritiesConfigs(
                randomString(),
                randomStringsAsSet(3).stream().map(Authority::new).collect(Collectors.toSet())
        );
    }

    public Set<String> getAllAuthoritiesValues() {
        return this.authorities.stream().map(Authority::getValue).collect(Collectors.toSet());
    }

    public Set<String> getAvailableAuthorities() {
        return this.authorities.stream()
                .map(Authority::getValue)
                .filter(authority -> !SUPER_ADMIN.equals(authority))
                .collect(Collectors.toSet());
    }
}
