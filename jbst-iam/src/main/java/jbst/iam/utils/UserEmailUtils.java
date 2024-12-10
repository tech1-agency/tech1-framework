package jbst.iam.utils;

import jbst.foundation.domain.base.Username;
import jbst.foundation.domain.http.requests.UserRequestMetadata;
import jbst.foundation.services.emails.domain.EmailHTML;
import jbst.iam.domain.enums.AccountAccessMethod;
import jbst.iam.domain.functions.FunctionEmailConfirmation;

import java.util.Map;

public interface UserEmailUtils {
    String getSubject(String eventName);
    EmailHTML getEmailConfirmationHTML(FunctionEmailConfirmation function);
    String getPasswordResetTemplateName();
    String getAuthenticationLoginTemplateName();
    String getSessionRefreshedTemplateName();
    Map<String, Object> getPasswordResetVariables(
            Username username,
            String token
    );
    Map<String, Object> getAuthenticationLoginOrSessionRefreshedVariables(
            Username username,
            UserRequestMetadata userRequestMetadata,
            AccountAccessMethod accountAccessMethod
    );
}
