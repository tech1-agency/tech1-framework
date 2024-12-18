package jbst.iam.validators;

import jbst.iam.domain.dto.requests.RequestUserChangePasswordBasic;
import jbst.iam.domain.dto.requests.RequestUserUpdate1;
import jbst.foundation.domain.base.Username;

public interface BaseUsersValidator {
    void validateUserUpdateRequest1(Username username, RequestUserUpdate1 request);
    void validateUserChangePasswordRequestBasic(RequestUserChangePasswordBasic request);
}
