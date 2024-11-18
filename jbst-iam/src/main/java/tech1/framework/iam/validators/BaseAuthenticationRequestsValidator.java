package tech1.framework.iam.validators;

import tech1.framework.iam.domain.dto.requests.RequestUserLogin;

public interface BaseAuthenticationRequestsValidator {
    void validateLoginRequest(RequestUserLogin requestUserLogin);}
