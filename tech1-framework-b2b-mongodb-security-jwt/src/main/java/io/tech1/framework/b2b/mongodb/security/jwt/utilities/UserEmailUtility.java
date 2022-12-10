package io.tech1.framework.b2b.mongodb.security.jwt.utilities;

import io.tech1.framework.b2b.mongodb.security.jwt.domain.enums.AccountAccessMethod;
import io.tech1.framework.domain.base.Username;
import io.tech1.framework.domain.http.requests.UserRequestMetadata;

import java.util.Map;

public interface UserEmailUtility {
    String getSubject(String eventName);
    String getAuthenticationLoginTemplateName();
    String getSessionRefreshedTemplateName();
    Map<String, Object> getAuthenticationLoginOrSessionRefreshedVariables(
            Username username,
            UserRequestMetadata userRequestMetadata,
            AccountAccessMethod accountAccessMethod
    );
}
