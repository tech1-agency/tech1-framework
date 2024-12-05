package jbst.iam.services;

import jbst.foundation.domain.exceptions.tokens.UserEmailConfirmException;

public interface BaseUsersTokensService {
    void confirmEmail(String token) throws UserEmailConfirmException;
}
