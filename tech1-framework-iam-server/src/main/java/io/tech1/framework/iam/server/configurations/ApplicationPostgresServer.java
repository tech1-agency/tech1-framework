package io.tech1.framework.iam.server.configurations;

import io.tech1.framework.iam.configurations.ApplicationPostgres;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.context.annotation.Profile;

@Profile("postgres")
@Configuration
@Import({
        ApplicationPostgres.class
})
@ComponentScan({
        "io.tech1.framework.iam.server.postgres"
})
public class ApplicationPostgresServer {
}
