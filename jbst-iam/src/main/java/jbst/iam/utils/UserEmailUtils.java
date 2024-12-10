package jbst.iam.utils;

import jbst.iam.domain.enums.AccountAccessMethod;
import jbst.foundation.domain.base.Username;
import jbst.foundation.domain.http.requests.UserRequestMetadata;

import java.util.Map;

public interface UserEmailUtils {
    String getSubject(String eventName);
    String getEmailConfirmationTemplateName();
    String getPasswordResetTemplateName();
    String getAuthenticationLoginTemplateName();
    String getSessionRefreshedTemplateName();
    Map<String, Object> getConfirmEmailVariables(
            Username username,
            String token
    );
    Map<String, Object> getResetPasswordVariables(
            Username username,
            String token
    );
    Map<String, Object> getAuthenticationLoginOrSessionRefreshedVariables(
            Username username,
            UserRequestMetadata userRequestMetadata,
            AccountAccessMethod accountAccessMethod
    );
}
