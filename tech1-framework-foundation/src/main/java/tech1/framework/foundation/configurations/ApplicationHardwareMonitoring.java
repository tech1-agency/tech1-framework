package tech1.framework.foundation.configurations;

import tech1.framework.foundation.domain.base.PropertyId;
import tech1.framework.foundation.domain.properties.ApplicationFrameworkProperties;
import tech1.framework.foundation.services.hardware.publishers.HardwareMonitoringPublisher;
import tech1.framework.foundation.services.hardware.publishers.impl.HardwareMonitoringPublisherImpl;
import tech1.framework.foundation.services.hardware.resources.HardwareMonitoringResource;
import tech1.framework.foundation.services.hardware.store.HardwareMonitoringStore;
import tech1.framework.foundation.services.hardware.store.impl.HardwareMonitoringStoreImpl;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class ApplicationHardwareMonitoring {

    // Spring Publisher
    private final ApplicationEventPublisher applicationEventPublisher;
    // Properties
    private final ApplicationFrameworkProperties applicationFrameworkProperties;

    @PostConstruct
    public void init() {
        this.applicationFrameworkProperties.getHardwareMonitoringConfigs().assertProperties(new PropertyId("hardwareMonitoringConfigs"));
    }

    @Bean
    HardwareMonitoringStore hardwareMonitoringStore() {
        return new HardwareMonitoringStoreImpl(
                this.applicationFrameworkProperties
        );
    }

    @Bean
    HardwareMonitoringPublisher hardwareMonitoringPublisher() {
        return new HardwareMonitoringPublisherImpl(
                this.applicationEventPublisher
        );
    }

    @Bean
    HardwareMonitoringResource hardwareMonitoringResource() {
        return new HardwareMonitoringResource(
                this.hardwareMonitoringPublisher()
        );
    }
}
