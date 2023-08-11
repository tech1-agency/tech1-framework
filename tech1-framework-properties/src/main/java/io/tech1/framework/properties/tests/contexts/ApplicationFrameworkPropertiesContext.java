package io.tech1.framework.properties.tests.contexts;

import io.tech1.framework.properties.ApplicationFrameworkProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import static io.tech1.framework.domain.tests.constants.TestsPropertiesConstants.*;

@Configuration
public class ApplicationFrameworkPropertiesContext {

    @Bean
    public ApplicationFrameworkProperties applicationFrameworkProperties() {
        var properties = new ApplicationFrameworkProperties();
        properties.setServerConfigs(SERVER_CONFIGS);
        properties.setAsyncConfigs(ASYNC_CONFIGS);
        properties.setEventsConfigs(EVENTS_CONFIGS);
        properties.setMvcConfigs(MVC_CONFIGS);
        properties.setEmailConfigs(EMAIL_CONFIGS);
        properties.setIncidentConfigs(INCIDENT_CONFIGS);
        properties.setHardwareMonitoringConfigs(HARDWARE_MONITORING_CONFIGS);
        properties.setHardwareServerConfigs(HARDWARE_SERVER_CONFIGS);
        properties.setSecurityJwtConfigs(SECURITY_JWT_CONFIGS);
        properties.setSecurityJwtWebsocketsConfigs(SECURITY_JWT_WEBSOCKETS_CONFIGS);
        properties.setMongodbSecurityJwtConfigs(MONGODB_SECURITY_JWT_CONFIGS);
        return properties;
    }
}
