package io.tech1.framework.hardware.configurations;

import io.tech1.framework.domain.base.PropertyId;
import io.tech1.framework.properties.ApplicationFrameworkProperties;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

import jakarta.annotation.PostConstruct;

@Configuration
@ComponentScan({
        // -------------------------------------------------------------------------------------------------------------
        "io.tech1.framework.hardware.monitoring"
        // -------------------------------------------------------------------------------------------------------------
})
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class ApplicationHardwareMonitoring {

    // Properties
    private final ApplicationFrameworkProperties applicationFrameworkProperties;

    @PostConstruct
    public void init() {
        this.applicationFrameworkProperties.getHardwareMonitoringConfigs().assertProperties(new PropertyId("hardwareMonitoringConfigs"));
    }
}
