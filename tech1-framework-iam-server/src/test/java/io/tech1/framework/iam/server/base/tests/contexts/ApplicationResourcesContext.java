package io.tech1.framework.iam.server.base.tests.contexts;

import io.tech1.framework.foundation.utilities.environment.EnvironmentUtility;
import io.tech1.framework.iam.server.base.services.UsersService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

import static org.mockito.Mockito.mock;

@Configuration
@ComponentScan({
        "io.tech1.framework.iam.server.base.resources"
})
@EnableWebMvc
public class ApplicationResourcesContext {

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
