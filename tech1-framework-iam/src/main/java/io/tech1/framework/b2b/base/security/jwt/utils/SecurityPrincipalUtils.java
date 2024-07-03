package io.tech1.framework.b2b.base.security.jwt.utils;

import io.tech1.framework.b2b.base.security.jwt.domain.jwt.JwtUser;

public interface SecurityPrincipalUtils {
    JwtUser getAuthenticatedJwtUser();
    String getAuthenticatedUsername();
    String getAuthenticatedUsernameOrUnexpected();
}
