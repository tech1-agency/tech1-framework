package io.tech1.framework.iam.validators.base;

import io.tech1.framework.iam.domain.dto.requests.RequestUserLogin;
import io.tech1.framework.iam.validators.BaseAuthenticationRequestsValidator;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import static io.tech1.framework.foundation.domain.asserts.Asserts.assertNonNullOrThrow;
import static io.tech1.framework.foundation.utilities.exceptions.ExceptionsMessagesUtility.invalidAttribute;

@Slf4j
@Component
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class BaseBaseAuthenticationRequestsValidatorImpl implements BaseAuthenticationRequestsValidator {

    @Override
    public void validateLoginRequest(RequestUserLogin requestUserLogin) {
        var username = requestUserLogin.username();
        var password = requestUserLogin.password();

        assertNonNullOrThrow(username, invalidAttribute("username"));
        assertNonNullOrThrow(password, invalidAttribute("password"));
    }
}
