package io.tech1.framework.b2b.base.security.jwt.constants;

import io.tech1.framework.foundation.domain.base.AbstractAuthority;
import lombok.experimental.UtilityClass;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

@UtilityClass
public class SecurityJwtConstants {
    public static final SimpleGrantedAuthority SUPERADMIN = new SimpleGrantedAuthority(AbstractAuthority.SUPERADMIN);
}
