package jbst.iam.server.configurations;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import tech1.framework.foundation.utilities.environment.EnvironmentUtility;
import jbst.iam.server.base.services.UsersService;

import static org.mockito.Mockito.mock;

@Configuration
@ComponentScan({
        "jbst.iam.server.base.resources"
})
@EnableWebMvc
public class TestsConfigurationResources {

    // Services
    @Bean
    UsersService usersService() {
        return mock(UsersService.class);
    }

    // Utilities
    @Bean
    EnvironmentUtility environmentUtils() {
        return mock(EnvironmentUtility.class);
    }
}
