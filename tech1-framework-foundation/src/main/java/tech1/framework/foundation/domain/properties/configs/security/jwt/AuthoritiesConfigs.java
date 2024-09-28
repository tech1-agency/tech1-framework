package tech1.framework.foundation.domain.properties.configs.security.jwt;

import tech1.framework.foundation.domain.properties.annotations.MandatoryProperty;
import tech1.framework.foundation.domain.properties.base.AbstractPropertyConfigs;
import tech1.framework.foundation.domain.properties.base.Authority;
import tech1.framework.foundation.domain.base.AbstractAuthority;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.boot.context.properties.bind.ConstructorBinding;

import java.util.Set;
import java.util.stream.Collectors;

import static tech1.framework.foundation.utilities.random.RandomUtility.randomString;
import static tech1.framework.foundation.utilities.random.RandomUtility.randomStringsAsSet;

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
                "tech1",
                Set.of(
                        new Authority(AbstractAuthority.SUPERADMIN),
                        new Authority("admin"),
                        new Authority("user"),
                        new Authority(AbstractAuthority.INVITATION_CODE_READ),
                        new Authority(AbstractAuthority.INVITATION_CODE_WRITE)
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
                .filter(authority -> !AbstractAuthority.SUPERADMIN.equals(authority))
                .collect(Collectors.toSet());
    }
}
