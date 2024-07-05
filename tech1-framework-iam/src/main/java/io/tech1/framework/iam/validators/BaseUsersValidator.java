package io.tech1.framework.iam.validators;

import io.tech1.framework.iam.domain.dto.requests.RequestUserChangePasswordBasic;
import io.tech1.framework.iam.domain.dto.requests.RequestUserUpdate1;
import io.tech1.framework.foundation.domain.base.Username;

public interface BaseUsersValidator {
    void validateUserUpdateRequest1(Username username, RequestUserUpdate1 request);
    void validateUserChangePasswordRequestBasic(RequestUserChangePasswordBasic request);
}
