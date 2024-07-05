package io.tech1.framework.iam.utils;

import io.tech1.framework.iam.domain.jwt.JwtUser;

public interface SecurityPrincipalUtils {
    JwtUser getAuthenticatedJwtUser();
    String getAuthenticatedUsername();
    String getAuthenticatedUsernameOrUnexpected();
}
