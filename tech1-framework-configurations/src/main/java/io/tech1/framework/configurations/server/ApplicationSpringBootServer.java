package io.tech1.framework.configurations.server;

import io.tech1.framework.utilities.environment.EnvironmentUtility;
import io.tech1.framework.utilities.environment.base.BaseEnvironmentUtility;
import io.tech1.framework.utilities.resources.actuator.BaseInfoResource;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;

@Configuration
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class ApplicationSpringBootServer {
    private final Environment environment;

    @Bean
    public EnvironmentUtility environmentUtility() {
        return new BaseEnvironmentUtility(
                this.environment
        );
    }

    @Bean
    public BaseInfoResource baseInfoResource() {
        return new BaseInfoResource(
                this.environmentUtility()
        );
    }
}
