package jbst.iam.utils;

import jbst.iam.domain.enums.AccountAccessMethod;
import tech1.framework.foundation.domain.base.Username;
import tech1.framework.foundation.domain.http.requests.UserRequestMetadata;

import java.util.Map;

public interface UserEmailUtils {
    String getSubject(String eventName);
    String getAuthenticationLoginTemplateName();
    String getSessionRefreshedTemplateName();
    Map<String, Object> getAuthenticationLoginOrSessionRefreshedVariables(
            Username username,
            UserRequestMetadata userRequestMetadata,
            AccountAccessMethod accountAccessMethod
    );
}
