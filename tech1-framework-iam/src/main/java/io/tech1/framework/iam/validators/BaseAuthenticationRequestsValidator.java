package io.tech1.framework.iam.validators;

import io.tech1.framework.iam.domain.dto.requests.RequestUserLogin;

public interface BaseAuthenticationRequestsValidator {
    void validateLoginRequest(RequestUserLogin requestUserLogin);}
