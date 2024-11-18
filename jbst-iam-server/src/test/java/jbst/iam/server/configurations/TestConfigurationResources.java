package jbst.iam.server.configurations;

import jbst.iam.server.base.services.UsersService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import jbst.foundation.utilities.environment.EnvironmentUtility;

import static org.mockito.Mockito.mock;

@Configuration
@ComponentScan({
        "jbst.iam.server.base.resources"
})
@EnableWebMvc
public class TestConfigurationResources {

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
