package jbst.iam.services.base;

import jbst.foundation.domain.properties.JbstProperties;
import jbst.foundation.services.emails.services.EmailService;
import jbst.iam.domain.functions.FunctionAuthenticationLoginEmail;
import jbst.iam.domain.functions.FunctionEmailConfirmation;
import jbst.iam.domain.functions.FunctionPasswordReset;
import jbst.iam.domain.functions.FunctionSessionRefreshedEmail;
import jbst.iam.services.UsersEmailsService;
import jbst.iam.utils.UserEmailUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class BaseUsersEmailsService implements UsersEmailsService {

    // Services
    private final EmailService emailService;
    // Utils
    private final UserEmailUtils userEmailUtils;
    // Properties
    private final JbstProperties jbstProperties;

    @Override
    public void executeEmailConfirmation(FunctionEmailConfirmation function) {
        var emailHTML = this.userEmailUtils.getEmailConfirmationHTML(function);
        this.emailService.sendHTML(emailHTML);
    }

    @Override
    public void executePasswordReset(FunctionPasswordReset function) {
        var emailHTML = this.userEmailUtils.getPasswordResetHTML(function);
        this.emailService.sendHTML(emailHTML);
    }

    @Override
    public void executeAuthenticationLogin(FunctionAuthenticationLoginEmail function) {
        if (!this.jbstProperties.getSecurityJwtConfigs().getUsersEmailsConfigs().getAuthenticationLogin().isEnabled()) {
            return;
        }
        var emailHTML = this.userEmailUtils.getAccountAccessedHTML(function.getFunctionAccountAccessed());
        this.emailService.sendHTML(emailHTML);
    }

    @Override
    public void executeSessionRefreshed(FunctionSessionRefreshedEmail function) {
        if (!this.jbstProperties.getSecurityJwtConfigs().getUsersEmailsConfigs().getSessionRefreshed().isEnabled()) {
            return;
        }
        var emailHTML = this.userEmailUtils.getAccountAccessedHTML(function.getFunctionAccountAccessed());
        this.emailService.sendHTML(emailHTML);
    }
}
