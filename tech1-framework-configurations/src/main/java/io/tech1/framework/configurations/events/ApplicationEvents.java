package io.tech1.framework.configurations.events;

import io.tech1.framework.properties.ApplicationFrameworkProperties;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.event.ApplicationEventMulticaster;
import org.springframework.context.event.SimpleApplicationEventMulticaster;
import org.springframework.core.task.SimpleAsyncTaskExecutor;

import javax.annotation.PostConstruct;

import static io.tech1.framework.domain.properties.utilities.PropertiesAsserter.assertProperties;

@Configuration
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class ApplicationEvents {

    // Properties
    private final ApplicationFrameworkProperties applicationFrameworkProperties;

    @PostConstruct
    public void init() {
        assertProperties(this.applicationFrameworkProperties.getEventsConfigs(), "eventsConfigs");
    }

    @Bean(name = "applicationEventMulticaster")
    public ApplicationEventMulticaster simpleApplicationEventMulticaster() {
        var threadNamePrefix = this.applicationFrameworkProperties.getEventsConfigs().getThreadNamePrefix();
        var simpleAsyncTaskExecutor = new SimpleAsyncTaskExecutor();
        simpleAsyncTaskExecutor.setThreadNamePrefix(threadNamePrefix);
        var eventMulticaster = new SimpleApplicationEventMulticaster();
        eventMulticaster.setTaskExecutor(simpleAsyncTaskExecutor);
        return eventMulticaster;
    }
}
