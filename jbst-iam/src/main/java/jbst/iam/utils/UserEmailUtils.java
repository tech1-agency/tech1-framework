package jbst.iam.utils;

import jbst.foundation.services.emails.domain.EmailHTML;
import jbst.iam.domain.functions.FunctionAccountAccessed;
import jbst.iam.domain.functions.FunctionEmailConfirmation;
import jbst.iam.domain.functions.FunctionPasswordReset;
import org.jetbrains.annotations.NotNull;

public interface UserEmailUtils {
    String getSubject(@NotNull String eventName);
    EmailHTML getAccountAccessedHTML(@NotNull FunctionAccountAccessed function);
    EmailHTML getEmailConfirmationHTML(@NotNull FunctionEmailConfirmation function);
    EmailHTML getPasswordResetHTML(@NotNull FunctionPasswordReset function);
}
