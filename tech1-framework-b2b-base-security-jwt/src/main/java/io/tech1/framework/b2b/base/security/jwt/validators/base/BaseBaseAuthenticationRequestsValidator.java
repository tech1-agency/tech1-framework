package io.tech1.framework.b2b.base.security.jwt.validators.base;

import io.tech1.framework.b2b.base.security.jwt.domain.dto.requests.RequestUserLogin;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import static io.tech1.framework.domain.asserts.Asserts.assertNonNullOrThrow;
import static io.tech1.framework.domain.utilities.exceptions.ExceptionsMessagesUtility.invalidAttribute;

@Slf4j
@Component
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class BaseBaseAuthenticationRequestsValidator implements io.tech1.framework.b2b.base.security.jwt.validators.BaseAuthenticationRequestsValidator {

    @Override
    public void validateLoginRequest(RequestUserLogin requestUserLogin) {
        var username = requestUserLogin.username();
        var password = requestUserLogin.password();

        assertNonNullOrThrow(username, invalidAttribute("username"));
        assertNonNullOrThrow(password, invalidAttribute("password"));
    }
}
