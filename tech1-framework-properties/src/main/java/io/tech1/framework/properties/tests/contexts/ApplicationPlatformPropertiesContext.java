package io.tech1.framework.properties.tests.contexts;

import io.tech1.framework.properties.ApplicationPlatformProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import static io.tech1.framework.domain.tests.constants.TestsPropertiesConstants.*;

@Configuration
public class ApplicationPlatformPropertiesContext {

    @Bean
    public ApplicationPlatformProperties applicationPlatformProperties() {
        var properties = new ApplicationPlatformProperties();
        properties.setAsyncConfigs(ASYNC_CONFIGS);
        properties.setEventsConfigs(EVENTS_CONFIGS);
        properties.setMvcConfigs(MVC_CONFIGS);
        properties.setEmailConfigs(EMAIL_CONFIGS);
        properties.setIncidentConfigs(INCIDENT_CONFIGS);
        properties.setHardwareMonitoringConfigs(HARDWARE_MONITORING_CONFIGS);
        properties.setHardwareServerConfigs(HARDWARE_SERVER_CONFIGS);
        properties.setSecurityJwtConfigs(SECURITY_JWT_CONFIGS);
        properties.setSecurityJwtWebsocketsConfigs(SECURITY_JWT_WEBSOCKETS_CONFIGS);
        return properties;
    }
}
