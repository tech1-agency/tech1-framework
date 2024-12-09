package jbst.iam.validators;

import jbst.foundation.domain.exceptions.tokens.UserTokenValidationException;
import jbst.iam.domain.dto.requests.RequestUserResetPassword;
import jbst.iam.domain.jwt.JwtUser;

public interface BaseUsersTokensRequestsValidator {
    void validateExecuteConfirmEmail(JwtUser user);
    void validateEmailConfirmationToken(String token) throws UserTokenValidationException;
    void validateExecuteResetPassword(JwtUser user);
    void validatePasswordReset(RequestUserResetPassword request) throws UserTokenValidationException;
}
