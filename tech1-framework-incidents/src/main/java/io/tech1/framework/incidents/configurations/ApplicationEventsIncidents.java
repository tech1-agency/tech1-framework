package io.tech1.framework.incidents.configurations;

import io.tech1.framework.incidents.handlers.ErrorHandlerPublisher;
import io.tech1.framework.properties.ApplicationFrameworkProperties;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.event.ApplicationEventMulticaster;
import org.springframework.context.event.SimpleApplicationEventMulticaster;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import javax.annotation.PostConstruct;

import static io.tech1.framework.domain.utilities.processors.ProcessorsUtility.getHalfOfCores;
import static io.tech1.framework.domain.utilities.processors.ProcessorsUtility.getNumOfCores;

@Configuration
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class ApplicationEventsIncidents {

    // Exceptions
    private final ErrorHandlerPublisher errorHandlerPublisher;
    // Properties
    private final ApplicationFrameworkProperties applicationFrameworkProperties;

    @PostConstruct
    public void init() {
        this.applicationFrameworkProperties.getEventsConfigs().assertProperties("eventsConfigs");
    }

    @Bean(name = "applicationEventMulticaster")
    public ApplicationEventMulticaster simpleApplicationEventMulticaster() {
        var threadNamePrefix = this.applicationFrameworkProperties.getEventsConfigs().getThreadNamePrefix();
        var taskExecutor = new ThreadPoolTaskExecutor();
        taskExecutor.setThreadNamePrefix(threadNamePrefix);
        taskExecutor.setCorePoolSize(getHalfOfCores());
        taskExecutor.setMaxPoolSize(getNumOfCores());
        taskExecutor.initialize();
        var eventMulticaster = new SimpleApplicationEventMulticaster();
        eventMulticaster.setTaskExecutor(taskExecutor);
        eventMulticaster.setErrorHandler(this.errorHandlerPublisher);
        return eventMulticaster;
    }
}
