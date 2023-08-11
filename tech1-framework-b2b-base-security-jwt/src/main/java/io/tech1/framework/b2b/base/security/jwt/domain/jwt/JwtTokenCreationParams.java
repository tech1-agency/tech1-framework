package io.tech1.framework.b2b.base.security.jwt.domain.jwt;

import io.tech1.framework.domain.base.Username;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.time.ZoneId;
import java.util.List;

public record JwtTokenCreationParams(
        Username username,
        List<SimpleGrantedAuthority> authorities,
        ZoneId zoneId
) {
}
