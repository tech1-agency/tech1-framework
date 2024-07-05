package io.tech1.framework.iam.server.postgres.tests.contexts;

import io.tech1.framework.iam.server.postgres.services.UsersService;
import io.tech1.framework.foundation.utilities.environment.EnvironmentUtility;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

import static org.mockito.Mockito.mock;

@Configuration
@ComponentScan({
        "io.tech1.framework.iam.server.postgres.resources"
})
@EnableWebMvc
public class ApplicationPostgresResourcesContext {

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
