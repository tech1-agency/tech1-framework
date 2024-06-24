package io.tech1.framework.domain.properties;

import io.tech1.framework.domain.properties.configs.*;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ApplicationFrameworkPropertiesTestsHardcodedContext {

    @Bean
    public ApplicationFrameworkProperties applicationFrameworkProperties() {
        var properties = new ApplicationFrameworkProperties();
        properties.setMavenConfigs(MavenConfigs.testsHardcoded());
        properties.setServerConfigs(ServerConfigs.testsHardcoded());
        properties.setUtilitiesConfigs(UtilitiesConfigs.testsHardcoded());
        properties.setAsyncConfigs(AsyncConfigs.testsHardcoded());
        properties.setEventsConfigs(EventsConfigs.testsHardcoded());
        properties.setMvcConfigs(MvcConfigs.testsHardcoded());
        properties.setEmailConfigs(EmailConfigs.testsHardcoded());
        properties.setIncidentConfigs(IncidentConfigs.testsHardcoded());
        properties.setHardwareMonitoringConfigs(HardwareMonitoringConfigs.testsHardcoded());
        properties.setHardwareServerConfigs(HardwareServerConfigs.testsHardcoded());
        properties.setSecurityJwtConfigs(SecurityJwtConfigs.testsHardcoded());
        properties.setSecurityJwtWebsocketsConfigs(SecurityJwtWebsocketsConfigs.testsHardcoded());
        properties.setMongodbSecurityJwtConfigs(MongodbSecurityJwtConfigs.testsHardcoded());
        return properties;
    }
}
