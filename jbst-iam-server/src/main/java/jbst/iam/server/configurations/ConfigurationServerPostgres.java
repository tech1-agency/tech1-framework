package jbst.iam.server.configurations;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.context.annotation.Profile;
import jbst.iam.configurations.ApplicationPostgres;

@Profile("postgres")
@Configuration
@Import({
        ApplicationPostgres.class
})
@ComponentScan({
        "jbst.iam.server.postgres"
})
public class ConfigurationServerPostgres {
}
