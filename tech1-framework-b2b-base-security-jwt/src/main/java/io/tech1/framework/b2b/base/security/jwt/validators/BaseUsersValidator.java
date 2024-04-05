package io.tech1.framework.b2b.base.security.jwt.validators;

import io.tech1.framework.b2b.base.security.jwt.domain.dto.requests.RequestUserChangePasswordBasic;
import io.tech1.framework.b2b.base.security.jwt.domain.dto.requests.RequestUserUpdate1;
import io.tech1.framework.domain.base.Username;

public interface BaseUsersValidator {
    void validateUserUpdateRequest1(Username username, RequestUserUpdate1 request);
    void validateUserChangePasswordRequestBasic(RequestUserChangePasswordBasic request);
}
