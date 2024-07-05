package io.tech1.framework.iam.server.postgres.configurations;

import io.tech1.framework.foundation.configurations.ApplicationHardwareMonitoring;
import io.tech1.framework.foundation.domain.base.PropertyId;
import io.tech1.framework.foundation.services.hardware.store.HardwareMonitoringStore;
import io.tech1.framework.foundation.services.hardware.subscribers.HardwareMonitoringSubscriber;
import io.tech1.framework.foundation.services.hardware.subscribers.impl.BaseHardwareMonitoringSubscriber;
import io.tech1.framework.iam.configurations.AbstractApplicationSecurityJwtConfigurer;
import io.tech1.framework.iam.configurations.ApplicationBaseSecurityJwt;
import io.tech1.framework.iam.configurations.ApplicationPostgres;
import io.tech1.framework.iam.server.postgres.properties.ApplicationProperties;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.data.mongo.MongoDataAutoConfiguration;
import org.springframework.boot.autoconfigure.mongo.MongoAutoConfiguration;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;

@Configuration
@ComponentScan({
        // -------------------------------------------------------------------------------------------------------------
        "io.tech1.framework.iam.filters.jwt_extension",
        // -------------------------------------------------------------------------------------------------------------
})
@Import({
        ApplicationHardwareMonitoring.class,
        ApplicationBaseSecurityJwt.class,
        ApplicationPostgres.class
})
@EnableConfigurationProperties({
        ApplicationProperties.class
})
@EnableAutoConfiguration(exclude = {
        MongoAutoConfiguration.class,
        MongoDataAutoConfiguration.class
})
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class ApplicationTech1 implements AbstractApplicationSecurityJwtConfigurer {

    // Store
    private final HardwareMonitoringStore hardwareMonitoringStore;
    // Properties
    private final ApplicationProperties applicationProperties;

    @PostConstruct
    public void init() {
        this.applicationProperties.getServerConfigs().assertProperties(new PropertyId("serverConfigs"));
    }

    @Override
    public void configure(WebSecurity webSecurity) {
        // no tech1-server configurations yet
    }

    @Override
    public void configure(HttpSecurity http) throws Exception {
        http.authorizeHttpRequests(authorizeHttpRequests ->
                authorizeHttpRequests
                        .requestMatchers("/hardware/**").permitAll()
        );
    }

    @Bean
    HardwareMonitoringSubscriber hardwareMonitoringSubscriber() {
        return new BaseHardwareMonitoringSubscriber(
                this.hardwareMonitoringStore
        );
    }
}
