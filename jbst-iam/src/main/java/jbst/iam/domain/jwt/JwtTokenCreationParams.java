package jbst.iam.domain.jwt;

import org.springframework.security.core.authority.SimpleGrantedAuthority;
import tech1.framework.foundation.domain.base.Username;

import java.time.ZoneId;
import java.util.Set;

public record JwtTokenCreationParams(
        Username username,
        Set<SimpleGrantedAuthority> authorities,
        ZoneId zoneId
) {
}
