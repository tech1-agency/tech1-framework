package io.tech1.framework.iam.configurations;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

@Configuration
@Import({
        ApplicationPostgresRepositories.class
})
@ComponentScan({
        "io.tech1.framework.iam.assistants.userdetails",
        "io.tech1.framework.iam.services",
        "io.tech1.framework.iam.sessions",
        "io.tech1.framework.iam.validators",
})
public class ApplicationPostgres {
}
