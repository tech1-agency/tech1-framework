package tech1.framework.foundation.resources.actuator;

import tech1.framework.foundation.domain.properties.ApplicationFrameworkProperties;
import tech1.framework.foundation.utilities.environment.EnvironmentUtility;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.actuate.info.Info;
import org.springframework.boot.actuate.info.InfoContributor;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@Component
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class BaseInfoResource implements InfoContributor {

    // Utilities
    private final EnvironmentUtility environmentUtility;
    // Properties
    private final ApplicationFrameworkProperties applicationFrameworkProperties;

    @Override
    public void contribute(Info.Builder builder) {
        Map<String, Object> details = new HashMap<>();
        details.put("activeProfile", this.environmentUtility.getOneActiveProfileOrDash());
        details.put("maven", this.applicationFrameworkProperties.getMavenConfigs().asMavenDetails());
        builder.withDetails(details);
    }
}
