package io.tech1.framework.b2b.mongodb.server.configurations;

import io.tech1.framework.hardware.configurations.ApplicationHardwareMonitoring;
import io.tech1.framework.hardware.monitoring.store.HardwareMonitoringStore;
import io.tech1.framework.hardware.monitoring.subscribers.HardwareMonitoringSubscriber;
import io.tech1.framework.hardware.monitoring.subscribers.impl.BaseHardwareMonitoringSubscriber;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

@Configuration
@ComponentScan({
        // -------------------------------------------------------------------------------------------------------------
        "io.tech1.framework.b2b.mongodb.security.jwt.essence",
        // -------------------------------------------------------------------------------------------------------------
})
@Import({
        ApplicationHardwareMonitoring.class
})
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class ApplicationPlatformTech1 {

    private final HardwareMonitoringStore hardwareMonitoringStore;

    @Bean
    public HardwareMonitoringSubscriber hardwareMonitoringSubscriber() {
        return new BaseHardwareMonitoringSubscriber(
                this.hardwareMonitoringStore
        );
    }
}
