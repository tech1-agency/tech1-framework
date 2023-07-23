package io.tech1.framework.b2b.mongodb.security.jwt.validators.impl;

import io.tech1.framework.b2b.mongodb.security.jwt.domain.dto.requests.RequestUserLogin;
import io.tech1.framework.b2b.mongodb.security.jwt.validators.AuthenticationRequestsValidator;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import static io.tech1.framework.domain.asserts.Asserts.assertNonNullNotBlankOrThrow;
import static io.tech1.framework.domain.utilities.exceptions.ExceptionsMessagesUtility.invalidAttribute;

@Slf4j
@Component
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class AuthenticationRequestsValidatorImpl implements AuthenticationRequestsValidator {

    @Override
    public void validateLoginRequest(RequestUserLogin requestUserLogin) {
        var username = requestUserLogin.username();
        var password = requestUserLogin.password();

        assertNonNullNotBlankOrThrow(username, invalidAttribute("username"));
        assertNonNullNotBlankOrThrow(password, invalidAttribute("password"));
    }
}
