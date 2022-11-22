package io.tech1.framework.domain.properties.configs.security.jwt;

import io.tech1.framework.domain.properties.annotations.MandatoryProperty;
import io.tech1.framework.domain.properties.base.Authority;
import io.tech1.framework.domain.properties.configs.AbstractPropertiesConfigs;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.Set;
import java.util.stream.Collectors;

import static io.tech1.framework.domain.base.AbstractAuthority.SUPER_ADMIN;

// Lombok (property-based)
@Data
@EqualsAndHashCode(callSuper = true)
public class AuthoritiesConfigs extends AbstractPropertiesConfigs {
    @MandatoryProperty
    private String packageName;
    @MandatoryProperty
    private Set<Authority> authorities;

    // NOTE: test-purposes
    public static AuthoritiesConfigs of(
            String packageName,
            Set<Authority> authorities
    ) {
        var instance = new AuthoritiesConfigs();
        instance.packageName = packageName;
        instance.authorities = authorities;
        return instance;
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
