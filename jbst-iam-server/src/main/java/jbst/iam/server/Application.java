package jbst.iam.server;

import jbst.foundation.domain.constants.JbstConstants;
import jbst.foundation.domain.properties.JbstProperties;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

import static jbst.foundation.domain.enums.Status.COMPLETED;

@Slf4j
@SpringBootApplication(scanBasePackages = {
        "jbst.iam.server.base",
        "jbst.iam.server.configurations"
})
@EnableConfigurationProperties({
        JbstProperties.class
})
public class Application {

    public static void main(String[] args) {
        var springApplication = new SpringApplication(Application.class);
        var applicationContext = springApplication.run(args);
        var jbstProperties = applicationContext.getBean(JbstProperties.class);
        var serverConfigs = jbstProperties.getServerConfigs();
        LOGGER.info(JbstConstants.Logs.getServerContainer(COMPLETED), serverConfigs.getName());
    }
}
