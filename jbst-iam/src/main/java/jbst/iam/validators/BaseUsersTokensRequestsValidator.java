package jbst.iam.validators;

import jbst.foundation.domain.exceptions.tokens.UserTokenValidationException;

public interface BaseUsersTokensRequestsValidator {
    void validateEmailConfirmationToken(String token) throws UserTokenValidationException;
}
