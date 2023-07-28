package io.tech1.framework.b2b.base.security.jwt.validators;

import io.tech1.framework.b2b.base.security.jwt.domain.dto.requests.RequestUserRegistration1;
import io.tech1.framework.domain.exceptions.authentication.RegistrationException;

public interface RegistrationRequestsValidator {
    void validateRegistrationRequest1(RequestUserRegistration1 requestUserRegistration1) throws RegistrationException;
}
