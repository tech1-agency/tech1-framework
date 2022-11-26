package io.tech1.framework.b2b.mongodb.security.jwt.utilities;

import io.tech1.framework.b2b.mongodb.security.jwt.domain.jwt.JwtUser;

public interface SecurityPrincipalUtility {
    JwtUser getAuthenticatedJwtUser();
    String getAuthenticatedUsername();
    String getAuthenticatedUsernameOrUnexpected();
}
