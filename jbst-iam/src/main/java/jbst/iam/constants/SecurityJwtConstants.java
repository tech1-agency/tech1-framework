package jbst.iam.constants;

import lombok.experimental.UtilityClass;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import jbst.foundation.domain.base.AbstractAuthority;

@UtilityClass
public class SecurityJwtConstants {
    public static final SimpleGrantedAuthority SUPERADMIN = new SimpleGrantedAuthority(AbstractAuthority.SUPERADMIN);
}
