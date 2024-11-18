package jbst.foundation.configurations;

import jakarta.annotation.PostConstruct;
import jbst.foundation.domain.base.PropertyId;
import jbst.foundation.domain.properties.JbstProperties;
import jbst.foundation.resources.actuator.BaseInfoResource;
import jbst.foundation.utilities.environment.EnvironmentUtility;
import jbst.foundation.utilities.environment.base.BaseEnvironmentUtility;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;

@Configuration
@EnableConfigurationProperties({
        JbstProperties.class
})
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class ConfigurationSpringBootServer {

    // Environment
    private final Environment environment;
    // Properties
    private final JbstProperties jbstProperties;

    @PostConstruct
    public void init() {
        this.jbstProperties.getServerConfigs().assertProperties(new PropertyId("serverConfigs"));
        this.jbstProperties.getMavenConfigs().assertProperties(new PropertyId("mavenConfigs"));
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
                this.jbstProperties
        );
    }
}
