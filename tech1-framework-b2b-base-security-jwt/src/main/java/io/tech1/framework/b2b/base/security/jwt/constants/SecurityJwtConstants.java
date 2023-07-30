package io.tech1.framework.b2b.base.security.jwt.constants;

import lombok.experimental.UtilityClass;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import static io.tech1.framework.domain.base.AbstractAuthority.SUPER_ADMIN;

@UtilityClass
public class SecurityJwtConstants {
    public static final SimpleGrantedAuthority SUPERADMIN = new SimpleGrantedAuthority(SUPER_ADMIN);
    public static final int DEFAULT_INVITATION_CODE_LENGTH = 40;
}
