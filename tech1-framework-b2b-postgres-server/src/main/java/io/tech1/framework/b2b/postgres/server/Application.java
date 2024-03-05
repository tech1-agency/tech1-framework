package io.tech1.framework.b2b.postgres.server;

import io.tech1.framework.b2b.postgres.server.properties.ApplicationProperties;
import io.tech1.framework.properties.ApplicationFrameworkProperties;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

import static io.tech1.framework.domain.constants.LogsConstants.SERVER_CONTAINER_1;
import static io.tech1.framework.domain.enums.Status.COMPLETED;

@Slf4j
@SpringBootApplication
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
        var mavenDetails = applicationFrameworkProperties.getMavenConfigs().asMavenDetails();
        LOGGER.info(SERVER_CONTAINER_1, serverConfigs.getName(), mavenDetails.version(), COMPLETED);
    }
}
