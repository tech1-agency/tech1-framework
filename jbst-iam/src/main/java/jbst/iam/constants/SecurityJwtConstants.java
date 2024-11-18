package jbst.iam.constants;

import tech1.framework.foundation.domain.base.AbstractAuthority;
import lombok.experimental.UtilityClass;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

@UtilityClass
public class SecurityJwtConstants {
    public static final SimpleGrantedAuthority SUPERADMIN = new SimpleGrantedAuthority(AbstractAuthority.SUPERADMIN);
}
