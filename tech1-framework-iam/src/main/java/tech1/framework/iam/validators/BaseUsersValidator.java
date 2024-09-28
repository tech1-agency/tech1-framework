package tech1.framework.iam.validators;

import tech1.framework.iam.domain.dto.requests.RequestUserChangePasswordBasic;
import tech1.framework.iam.domain.dto.requests.RequestUserUpdate1;
import tech1.framework.foundation.domain.base.Username;

public interface BaseUsersValidator {
    void validateUserUpdateRequest1(Username username, RequestUserUpdate1 request);
    void validateUserChangePasswordRequestBasic(RequestUserChangePasswordBasic request);
}
