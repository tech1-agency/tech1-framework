package io.tech1.framework.b2b.postgres.security.jwt.constants;

import lombok.experimental.UtilityClass;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import static io.tech1.framework.domain.base.AbstractAuthority.SUPER_ADMIN;

@UtilityClass
public class UsersConstants {
    public static final SimpleGrantedAuthority SUPERADMIN = new SimpleGrantedAuthority(SUPER_ADMIN);
}
