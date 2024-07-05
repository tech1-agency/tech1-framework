package io.tech1.framework.iam.server.configurations;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

@Profile("mongodb")
@Configuration
@ComponentScan({
        "io.tech1.framework.iam.server.mongodb"
})
public class ApplicationMongoServer {
}
