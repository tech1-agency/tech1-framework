package jbst.iam.utils;

import jbst.iam.domain.jwt.JwtUser;

public interface SecurityPrincipalUtils {
    JwtUser getAuthenticatedJwtUser();
    String getAuthenticatedUsername();
    String getAuthenticatedUsernameOrUnexpected();
}
