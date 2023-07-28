package io.tech1.framework.b2b.mongodb.security.jwt.services.impl;

import io.tech1.framework.b2b.base.security.jwt.domain.enums.AccountAccessMethod;
import io.tech1.framework.b2b.base.security.jwt.domain.functions.FunctionAuthenticationLoginEmail;
import io.tech1.framework.b2b.base.security.jwt.domain.functions.FunctionSessionRefreshedEmail;
import io.tech1.framework.b2b.mongodb.security.jwt.services.UserEmailService;
import io.tech1.framework.b2b.base.security.jwt.utils.UserEmailUtils;
import io.tech1.framework.domain.base.Email;
import io.tech1.framework.domain.base.Username;
import io.tech1.framework.domain.http.requests.UserRequestMetadata;
import io.tech1.framework.domain.properties.base.Checkbox;
import io.tech1.framework.domain.tuples.Tuple3;
import io.tech1.framework.emails.domain.EmailHTML;
import io.tech1.framework.emails.services.EmailService;
import io.tech1.framework.properties.ApplicationFrameworkProperties;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Set;

import static java.util.Objects.nonNull;

@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class UserEmailServiceImpl implements UserEmailService {

    // Services
    private final EmailService emailService;
    // Utilities
    private final UserEmailUtils userEmailUtils;
    // Properties
    private final ApplicationFrameworkProperties applicationFrameworkProperties;

    @Override
    public void executeAuthenticationLogin(FunctionAuthenticationLoginEmail function) {
        this.executeBy(
                function.getTuple3(),
                this.applicationFrameworkProperties.getSecurityJwtConfigs().getUsersEmailsConfigs().getAuthenticationLogin(),
                this.userEmailUtils.getAuthenticationLoginTemplateName(),
                AccountAccessMethod.USERNAME_PASSWORD
        );
    }

    @Override
    public void executeSessionRefreshed(FunctionSessionRefreshedEmail function) {
        this.executeBy(
                function.getTuple3(),
                this.applicationFrameworkProperties.getSecurityJwtConfigs().getUsersEmailsConfigs().getSessionRefreshed(),
                this.userEmailUtils.getSessionRefreshedTemplateName(),
                AccountAccessMethod.SECURITY_TOKEN
        );
    }

    // =================================================================================================================
    // PRIVATE METHODS
    // =================================================================================================================
    private void executeBy(
            Tuple3<Username, Email, UserRequestMetadata> tuple3,
            Checkbox checkbox,
            String templateName,
            AccountAccessMethod accountAccessMethod
    ) {
        var email = tuple3.b();
        if (nonNull(email) && checkbox.isEnabled()) {
            this.emailService.sendHTML(
                    new EmailHTML(
                            Set.of(email.value()),
                            this.userEmailUtils.getSubject("Account Accessed"),
                            templateName,
                            this.userEmailUtils.getAuthenticationLoginOrSessionRefreshedVariables(
                                    tuple3.a(),
                                    tuple3.c(),
                                    accountAccessMethod
                            )
                    )
            );
        }
    }
}
