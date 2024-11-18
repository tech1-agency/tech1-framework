package jbst.iam.utils.impl;

import jbst.iam.domain.enums.AccountAccessMethod;
import jbst.iam.utils.UserEmailUtils;
import tech1.framework.foundation.domain.base.Username;
import tech1.framework.foundation.domain.http.requests.UserRequestMetadata;
import tech1.framework.foundation.domain.properties.ApplicationFrameworkProperties;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ResourceLoader;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

import static tech1.framework.foundation.domain.constants.DatetimeConstants.DTF11;
import static tech1.framework.foundation.utilities.time.LocalDateUtility.now;
import static java.time.ZoneOffset.UTC;

@Component
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class UserEmailUtilsImpl implements UserEmailUtils {

    // Resources
    private final ResourceLoader resourceLoader;
    // Properties
    private final ApplicationFrameworkProperties applicationFrameworkProperties;

    @Override
    public String getSubject(String eventName) {
        var prefix = this.applicationFrameworkProperties.getSecurityJwtConfigs().getUsersEmailsConfigs().getSubjectPrefix();
        var serverConfigs = this.applicationFrameworkProperties.getServerConfigs();
        var server = "\"" + serverConfigs.getName() + "\"";
        var time = LocalDateTime.now(UTC).format(DTF11) + " (UTC)";
        return prefix + " " + eventName + " on " + server + " — " + time;
    }

    @Override
    public String getAuthenticationLoginTemplateName() {
        return this.getServerOrFrameworkTemplateName(
                "server-authentication-login",
                "jbst-account-accessed"
        );
    }

    @Override
    public String getSessionRefreshedTemplateName() {
        return this.getServerOrFrameworkTemplateName(
                "server-session-refreshed",
                "jbst-account-accessed"
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
        variables.put("username", username.value());
        variables.put("accessMethod", accountAccessMethod.getValue());
        variables.put("where", geoLocation.getWhere());
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
