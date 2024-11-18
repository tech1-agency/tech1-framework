package tech1.framework.iam.server.base.tests.contexts;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import tech1.framework.foundation.utilities.environment.EnvironmentUtility;
import tech1.framework.iam.server.base.services.UsersService;

import static org.mockito.Mockito.mock;

@Configuration
@ComponentScan({
        "tech1.framework.iam.server.base.resources"
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
