package jbst.hardware.monitoring.server;

import jbst.foundation.domain.constants.JbstConstants;
import jbst.foundation.domain.enums.Status;
import jbst.foundation.domain.properties.JbstProperties;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import static jbst.foundation.domain.constants.JbstConstants.Logs.PREFIX;

@Slf4j
@SpringBootApplication
public class Application {

    public static void main(String[] args) {
        var springApplication = new SpringApplication(Application.class);
        var applicationContext = springApplication.run(args);
        var jbstProperties = applicationContext.getBean(JbstProperties.class);
        LOGGER.info(JbstConstants.Logs.getServerContainer(Status.COMPLETED), jbstProperties.getServerConfigs().getName());
        LOGGER.info(PREFIX + " TargetURL: {}", jbstProperties.getHardwareServerConfigs().getBaseURL());
    }
}
