package jbst.iam.utils.impl;

import jbst.foundation.domain.base.Username;
import jbst.foundation.domain.http.requests.UserRequestMetadata;
import jbst.foundation.domain.properties.JbstProperties;
import jbst.foundation.services.emails.domain.EmailHTML;
import jbst.iam.domain.enums.AccountAccessMethod;
import jbst.iam.domain.functions.FunctionEmailConfirmation;
import jbst.iam.utils.UserEmailUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.web.ServerProperties;
import org.springframework.core.io.ResourceLoader;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import static java.time.ZoneOffset.UTC;
import static jbst.foundation.domain.constants.JbstConstants.DateTimeFormatters.DTF11;
import static jbst.foundation.utilities.time.LocalDateUtility.now;

@Component
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class UserEmailUtilsImpl implements UserEmailUtils {

    // Resources
    private final ResourceLoader resourceLoader;
    // Properties
    private final JbstProperties jbstProperties;
    private final ServerProperties serverProperties;

    @Override
    public String getSubject(String eventName) {
        var prefix = this.jbstProperties.getSecurityJwtConfigs().getUsersEmailsConfigs().getSubjectPrefix();
        var time = LocalDateTime.now(UTC).format(DTF11) + " (UTC)";
        return prefix + " " + eventName + " at " + time;
    }

    @Override
    public EmailHTML getEmailConfirmationHTML(FunctionEmailConfirmation function) {
        var servletContextPath = this.serverProperties.getServlet().getContextPath();
        var serverURL = this.jbstProperties.getServerConfigs().getServerContextPathURL(servletContextPath);
        var basePathPrefix = this.jbstProperties.getMvcConfigs().getBasePathPrefix();
        var usersTokensConfigs = this.jbstProperties.getSecurityJwtConfigs().getUsersTokensConfigs();
        Map<String, Object> variables = new HashMap<>();
        variables.put("username", function.username().value());
        variables.put("confirmationLink", usersTokensConfigs.getEmailConfirmURL(serverURL, basePathPrefix, function.token()));
        variables.put("year", now(UTC).getYear());
        return new EmailHTML(
                Set.of(function.email().value()),
                this.getSubject("Email Confirmation"),
                this.getServerOrFallbackJbstTemplateName(
                        "server-email-confirmation",
                        "jbst-email-confirmation"
                ),
                variables
        );
    }

    @Override
    public String getPasswordResetTemplateName() {
        return this.getServerOrFallbackJbstTemplateName(
                "server-password-reset",
                "jbst-password-reset"
        );
    }

    @Override
    public String getAuthenticationLoginTemplateName() {
        return this.getServerOrFallbackJbstTemplateName(
                "server-authentication-login",
                "jbst-account-accessed"
        );
    }

    @Override
    public String getSessionRefreshedTemplateName() {
        return this.getServerOrFallbackJbstTemplateName(
                "server-session-refreshed",
                "jbst-account-accessed"
        );
    }

    @Override
    public Map<String, Object> getEmailConfirmationVariables(
            Username username,
            String token
    ) {
        var servletContextPath = this.serverProperties.getServlet().getContextPath();
        var serverURL = this.jbstProperties.getServerConfigs().getServerContextPathURL(servletContextPath);
        var basePathPrefix = this.jbstProperties.getMvcConfigs().getBasePathPrefix();
        var usersTokensConfigs = this.jbstProperties.getSecurityJwtConfigs().getUsersTokensConfigs();
        Map<String, Object> variables = new HashMap<>();
        variables.put("username", username.value());
        variables.put("confirmationLink", usersTokensConfigs.getEmailConfirmURL(serverURL, basePathPrefix, token));
        variables.put("year", now(UTC).getYear());
        return variables;
    }

    @Override
    public Map<String, Object> getPasswordResetVariables(Username username, String token) {
        var webclientURL = this.jbstProperties.getServerConfigs().getWebclientURL();
        var usersTokensConfigs = this.jbstProperties.getSecurityJwtConfigs().getUsersTokensConfigs();
        Map<String, Object> variables = new HashMap<>();
        variables.put("username", username.value());
        variables.put("resetPasswordLink", usersTokensConfigs.getPasswordResetURL(webclientURL, token));
        variables.put("year", now(UTC).getYear());
        return variables;
    }

    @Override
    public Map<String, Object> getAuthenticationLoginOrSessionRefreshedVariables(
            Username username,
            UserRequestMetadata userRequestMetadata,
            AccountAccessMethod accountAccessMethod
    ) {
        var geoLocation = userRequestMetadata.getGeoLocation();
        var userAgentDetails = userRequestMetadata.getUserAgentDetails();
        Map<String, Object> variables = new HashMap<>();
        variables.put("year", now(UTC).getYear());
        variables.put("username", username.value());
        variables.put("accessMethod", accountAccessMethod.getValue());
        variables.put("where", geoLocation.getWhere());
        variables.put("what", userAgentDetails.getWhat());
        variables.put("ipAddress", geoLocation.getIpAddr());
        variables.put("webclientURL", this.jbstProperties.getServerConfigs().getWebclientURL());
        return variables;
    }

    // =================================================================================================================
    // PRIVATE METHODS
    // =================================================================================================================
    private String getServerOrFallbackJbstTemplateName(String serverTemplateName, String jbstTemplateName) {
        var resource = this.resourceLoader.getResource("classpath:/email-templates/" + serverTemplateName + ".html");
        return resource.exists() ? serverTemplateName : jbstTemplateName;
    }
}
