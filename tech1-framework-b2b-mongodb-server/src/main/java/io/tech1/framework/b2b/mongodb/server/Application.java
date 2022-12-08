package io.tech1.framework.b2b.mongodb.server;

import io.tech1.framework.b2b.mongodb.server.properties.ApplicationProperties;
import io.tech1.framework.properties.ApplicationFrameworkProperties;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.data.mongo.MongoDataAutoConfiguration;
import org.springframework.boot.autoconfigure.mongo.MongoAutoConfiguration;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

import static io.tech1.framework.domain.constants.FrameworkConstants.VERSION_RUNTIME;
import static io.tech1.framework.domain.constants.LogsConstants.SERVER_CONTAINER;
import static io.tech1.framework.domain.enums.Status.COMPLETED;

@Slf4j
@SpringBootApplication(
        exclude = {
                MongoAutoConfiguration.class,
                MongoDataAutoConfiguration.class
        }
)
@EnableConfigurationProperties({
        ApplicationProperties.class,
        ApplicationFrameworkProperties.class
})
public class Application {

    public static void main(String[] args) {
        var springApplication = new SpringApplication(Application.class);
        var applicationContext = springApplication.run(args);
        var applicationFrameworkProperties = applicationContext.getBean(ApplicationFrameworkProperties.class);
        var serverConfigs = applicationFrameworkProperties.getServerConfigs();
        LOGGER.info(SERVER_CONTAINER, serverConfigs.getName(), VERSION_RUNTIME, COMPLETED);
    }
}
