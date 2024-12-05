package jbst.iam.validators.abtracts;

import jbst.foundation.domain.exceptions.tokens.UserTokenValidationException;
import jbst.iam.repositories.UsersTokensRepository;
import jbst.iam.validators.BaseUsersTokensRequestsValidator;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;

import static java.util.Objects.isNull;

@AllArgsConstructor(access = AccessLevel.PROTECTED)
public class AbstractBaseUsersTokensRequestsValidator implements BaseUsersTokensRequestsValidator {

    // Repositories
    protected final UsersTokensRepository usersTokensRepository;

    @Override
    public void validateEmailConfirmationToken(String token) throws UserTokenValidationException {
        var userToken = this.usersTokensRepository.findByValueAsAny(token);
        if (isNull(userToken)) {
            throw UserTokenValidationException.notFound();
        }
        if (userToken.used()) {
            throw UserTokenValidationException.used();
        }
        if (userToken.isExpired()) {
            throw UserTokenValidationException.expired();
        }
        if (!userToken.type().isEmailConfirmation()) {
            throw UserTokenValidationException.invalidType();
        }
    }

}
