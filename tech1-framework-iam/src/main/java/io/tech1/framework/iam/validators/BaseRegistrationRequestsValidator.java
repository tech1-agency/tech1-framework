package io.tech1.framework.iam.validators;

import io.tech1.framework.iam.domain.dto.requests.RequestUserRegistration1;
import io.tech1.framework.foundation.domain.exceptions.authentication.RegistrationException;

public interface BaseRegistrationRequestsValidator {
    void validateRegistrationRequest1(RequestUserRegistration1 request) throws RegistrationException;
}
