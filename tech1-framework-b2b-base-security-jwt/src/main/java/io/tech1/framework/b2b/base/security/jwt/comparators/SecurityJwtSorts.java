package io.tech1.framework.b2b.base.security.jwt.comparators;

import lombok.experimental.UtilityClass;
import org.springframework.data.domain.Sort;

@UtilityClass
public class SecurityJwtSorts {
    public static final Sort INVITATION_CODES_UNUSED = Sort.by("owner").ascending()
            .and(Sort.by("value").ascending());
}
