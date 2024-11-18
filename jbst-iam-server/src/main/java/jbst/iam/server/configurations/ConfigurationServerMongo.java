package jbst.iam.server.configurations;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.context.annotation.Profile;
import jbst.iam.configurations.ApplicationMongo;

@Profile("mongodb")
@Configuration
@Import({
        ApplicationMongo.class
})
@ComponentScan({
        "jbst.iam.server.mongodb"
})
public class ConfigurationServerMongo {
}
