package io.tech1.framework.iam.server.configurations;

import io.tech1.framework.iam.configurations.ApplicationMongo;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.context.annotation.Profile;

@Profile("mongodb")
@Configuration
@Import({
        ApplicationMongo.class
})
@ComponentScan({
        "io.tech1.framework.iam.server.mongodb"
})
public class ApplicationMongoServer {
}
