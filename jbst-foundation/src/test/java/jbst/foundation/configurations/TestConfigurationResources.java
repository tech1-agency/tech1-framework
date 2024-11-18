package jbst.foundation.configurations;

import jbst.foundation.services.hardware.publishers.HardwareMonitoringPublisher;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

import static org.mockito.Mockito.mock;

@Configuration
@ComponentScan({
        // -------------------------------------------------------------------------------------------------------------
        "jbst.foundation.services.hardware.resources"
        // -------------------------------------------------------------------------------------------------------------
})
@EnableWebMvc
public class TestConfigurationResources {
    @Bean
    HardwareMonitoringPublisher hardwareMonitoringPublisher() {
        return mock(HardwareMonitoringPublisher.class);
    }
}
