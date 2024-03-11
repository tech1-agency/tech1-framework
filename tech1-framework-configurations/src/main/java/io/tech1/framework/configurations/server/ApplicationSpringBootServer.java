package io.tech1.framework.configurations.server;

import io.tech1.framework.domain.base.PropertyId;
import io.tech1.framework.properties.ApplicationFrameworkProperties;
import io.tech1.framework.utilities.environment.EnvironmentUtility;
import io.tech1.framework.utilities.environment.base.BaseEnvironmentUtility;
import io.tech1.framework.utilities.resources.actuator.BaseInfoResource;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;

import javax.annotation.PostConstruct;

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
