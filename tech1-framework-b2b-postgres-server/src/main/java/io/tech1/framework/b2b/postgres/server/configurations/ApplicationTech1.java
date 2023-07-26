package io.tech1.framework.b2b.postgres.server.configurations;

import io.tech1.framework.b2b.postgres.security.jwt.configurations.ApplicationPostgres;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

@Configuration
@Import({
        ApplicationPostgres.class
})
public class ApplicationTech1 {
    // no beans yet
}
