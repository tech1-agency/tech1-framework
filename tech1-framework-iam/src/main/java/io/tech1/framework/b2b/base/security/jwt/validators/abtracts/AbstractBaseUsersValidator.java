package io.tech1.framework.b2b.base.security.jwt.validators.abtracts;

import io.tech1.framework.b2b.base.security.jwt.domain.dto.requests.RequestUserChangePasswordBasic;
import io.tech1.framework.b2b.base.security.jwt.domain.dto.requests.RequestUserUpdate1;
import io.tech1.framework.b2b.base.security.jwt.repositories.UsersRepository;
import io.tech1.framework.b2b.base.security.jwt.validators.BaseUsersValidator;
import io.tech1.framework.foundation.domain.base.Username;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;

import static io.tech1.framework.foundation.utilities.exceptions.ExceptionsMessagesUtility.entityAlreadyUsed;
import static java.util.Objects.nonNull;

@AllArgsConstructor(access = AccessLevel.PROTECTED)
public abstract class AbstractBaseUsersValidator implements BaseUsersValidator {
    // Repositories
    protected final UsersRepository usersRepository;

    @Override
    public void validateUserUpdateRequest1(Username username, RequestUserUpdate1 request) {
        var user = this.usersRepository.findByEmailAsJwtUserOrNull(request.email());
        if (nonNull(user) && !user.username().equals(username)) {
            throw new IllegalArgumentException(entityAlreadyUsed("Email", request.email().value()));
        }
    }

    @Override
    public void validateUserChangePasswordRequestBasic(RequestUserChangePasswordBasic request) {
        request.assertPasswordsOrThrow();
    }
}
