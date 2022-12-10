package io.tech1.framework.b2b.mongodb.security.jwt.utilities.impl;

import io.tech1.framework.b2b.mongodb.security.jwt.domain.enums.AccountAccessMethod;
import io.tech1.framework.b2b.mongodb.security.jwt.utilities.UserEmailUtility;
import io.tech1.framework.domain.base.Username;
import io.tech1.framework.domain.http.requests.UserRequestMetadata;
import io.tech1.framework.properties.ApplicationFrameworkProperties;
import io.tech1.framework.utilities.environment.EnvironmentUtility;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ResourceLoader;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;

import static io.tech1.framework.domain.utilities.time.LocalDateUtility.now;
import static java.time.ZoneOffset.UTC;

@Component
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class UserEmailUtilityImpl implements UserEmailUtility {

    private static final DateTimeFormatter DTF = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss");

    // Resources
    private final ResourceLoader resourceLoader;
    // Utilities
    private final EnvironmentUtility environmentUtility;
    // Properties
    private final ApplicationFrameworkProperties applicationFrameworkProperties;

    @Override
    public String getSubject(String eventName) {
        var prefix = this.applicationFrameworkProperties.getSecurityJwtConfigs().getUsersEmailsConfigs().getSubjectPrefix();
        var serverConfigs = this.applicationFrameworkProperties.getServerConfigs();
        var environment = this.environmentUtility.getActiveProfile();
        var server = "[" + serverConfigs.getName() + "@" + environment + "]";
        var time = LocalDateTime.now(UTC).format(DTF) + " (UTC)";
        return prefix + " " + eventName + " on " + server + " — " + time;
    }

    @Override
    public String getAuthenticationLoginTemplateName() {
        return this.getServerOrFrameworkTemplateName(
                "server-authentication-login",
                "framework-account-accessed"
        );
    }

    @Override
    public String getSessionRefreshedTemplateName() {
        return this.getServerOrFrameworkTemplateName(
                "server-session-refreshed",
                "framework-account-accessed"
        );
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
        variables.put("username", username.getIdentifier());
        variables.put("accessMethod", accountAccessMethod.getValue());
        variables.put("where", geoLocation.getCountryFlag() + " " + geoLocation.getWhere());
        variables.put("what", userAgentDetails.getWhat());
        variables.put("ipAddress", geoLocation.getIpAddr());
        variables.put("webclientURL", this.applicationFrameworkProperties.getServerConfigs().getWebclientURL());
        return variables;
    }

    // =================================================================================================================
    // PRIVATE METHODS
    // =================================================================================================================
    private String getServerOrFrameworkTemplateName(String serverTemplateName, String frameworkTemplateName) {
        var resource = this.resourceLoader.getResource("classpath:/email-templates/" + serverTemplateName + ".html");
        return resource.exists() ? serverTemplateName : frameworkTemplateName;
    }
}
