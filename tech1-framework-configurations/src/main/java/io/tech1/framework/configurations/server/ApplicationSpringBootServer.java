package io.tech1.framework.configurations.server;

import io.tech1.framework.foundation.domain.base.PropertyId;
import io.tech1.framework.foundation.domain.properties.ApplicationFrameworkProperties;
import io.tech1.framework.foundation.utilities.environment.EnvironmentUtility;
import io.tech1.framework.foundation.utilities.environment.base.BaseEnvironmentUtility;
import io.tech1.framework.foundation.resources.actuator.BaseInfoResource;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;

@Configuration
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class ApplicationSpringBootServer {

    // Environment
    private final Environment environment;
    // Properties
    private final ApplicationFrameworkProperties applicationFrameworkProperties;

    @PostConstruct
    public void init() {
        this.applicationFrameworkProperties.getServerConfigs().assertProperties(new PropertyId("serverConfigs"));
        this.applicationFrameworkProperties.getMavenConfigs().assertProperties(new PropertyId("mavenConfigs"));
    }

    @Bean
    public EnvironmentUtility environmentUtility() {
        return new BaseEnvironmentUtility(
                this.environment
        );
    }

    @Bean
    public BaseInfoResource baseInfoResource() {
        return new BaseInfoResource(
                this.environmentUtility(),
                this.applicationFrameworkProperties
        );
    }
}
