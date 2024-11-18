package jbst.iam.validators;

import jbst.iam.domain.dto.requests.RequestUserRegistration1;
import tech1.framework.foundation.domain.exceptions.authentication.RegistrationException;

public interface BaseRegistrationRequestsValidator {
    void validateRegistrationRequest1(RequestUserRegistration1 request) throws RegistrationException;
}
