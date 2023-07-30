package io.tech1.framework.b2b.postgres.security.jwt.configurations;

import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@Configuration
@ComponentScan({
        "io.tech1.framework.b2b.postgres.security.jwt.assistants.userdetails",
        "io.tech1.framework.b2b.postgres.security.jwt.sessions"
})
@EntityScan({
        "io.tech1.framework.b2b.postgres.security.jwt.domain.db"
})
@EnableJpaRepositories({
        "io.tech1.framework.b2b.postgres.security.jwt.repositories"
})
@EnableTransactionManagement
public class ApplicationPostgres {
}
