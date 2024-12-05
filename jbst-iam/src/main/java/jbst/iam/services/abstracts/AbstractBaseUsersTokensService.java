package jbst.iam.services.abstracts;

import jbst.foundation.domain.exceptions.tokens.UserEmailConfirmException;
import jbst.iam.domain.db.UserEmailDetails;
import jbst.iam.repositories.UsersRepository;
import jbst.iam.repositories.UsersTokensRepository;
import jbst.iam.services.BaseUsersTokensService;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;

import static java.util.Objects.isNull;

@AllArgsConstructor(access = AccessLevel.PROTECTED)
public class AbstractBaseUsersTokensService implements BaseUsersTokensService {

    // Repositories
    private final UsersTokensRepository usersTokensRepository;
    private final UsersRepository usersRepository;

    @Override
    public void confirmEmail(String token) throws UserEmailConfirmException {
        var userToken = this.usersTokensRepository.findByValueAsAny(token);
        if (isNull(userToken)) {
            throw UserEmailConfirmException.tokenNotFound();
        }
        var user = this.usersRepository.findByUsernameAsJwtUserOrNull(userToken.username());
        if (isNull(user)) {
            throw UserEmailConfirmException.userNotFound();
        }
        user = user.withEmailDetails(UserEmailDetails.confirmed());
        this.usersRepository.saveAs(user);
        userToken = userToken.withUsed(true);
        this.usersTokensRepository.saveAs(userToken);
    }

}
