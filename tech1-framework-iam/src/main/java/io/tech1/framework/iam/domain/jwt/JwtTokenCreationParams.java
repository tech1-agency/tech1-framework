package io.tech1.framework.iam.domain.jwt;

import io.tech1.framework.foundation.domain.base.Username;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.time.ZoneId;
import java.util.Set;

public record JwtTokenCreationParams(
        Username username,
        Set<SimpleGrantedAuthority> authorities,
        ZoneId zoneId
) {
}
