package io.tech1.framework.b2b.postgres.server.configurations;

import io.tech1.framework.b2b.postgres.security.jwt.configurations.ApplicationPostgres;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@Configuration
@Import({
        ApplicationPostgres.class
})
@EntityScan({
        "io.tech1.framework.b2b.postgres.server.domain.db"
})
@EnableJpaRepositories({
        "io.tech1.framework.b2b.postgres.server.repositories"
})
public class ApplicationTech1 {
    // no beans yet
}
