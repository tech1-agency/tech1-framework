package io.tech1.framework.b2b.base.security.jwt.validators;

import io.tech1.framework.b2b.base.security.jwt.domain.dto.requests.RequestUserChangePassword1;
import io.tech1.framework.b2b.base.security.jwt.domain.dto.requests.RequestUserUpdate1;
import io.tech1.framework.b2b.base.security.jwt.domain.dto.requests.RequestUserUpdate2;
import io.tech1.framework.domain.base.Username;

public interface BaseUsersValidator {
    void validateUserUpdateRequest1(Username username, RequestUserUpdate1 requestUserUpdate1);
    void validateUserUpdateRequest2(RequestUserUpdate2 requestUserUpdate2);
    void validateUserChangePasswordRequest1(RequestUserChangePassword1 requestUserChangePassword1);
}
