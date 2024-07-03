package io.tech1.framework.b2b.base.security.jwt.domain.jwt;

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
