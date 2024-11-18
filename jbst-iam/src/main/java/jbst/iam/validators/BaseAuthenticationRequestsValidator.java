package jbst.iam.validators;

import jbst.iam.domain.dto.requests.RequestUserLogin;

public interface BaseAuthenticationRequestsValidator {
    void validateLoginRequest(RequestUserLogin requestUserLogin);}
