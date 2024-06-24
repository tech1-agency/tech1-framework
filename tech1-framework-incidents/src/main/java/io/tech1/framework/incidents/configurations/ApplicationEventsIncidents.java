package io.tech1.framework.incidents.configurations;

import io.tech1.framework.domain.base.PropertyId;
import io.tech1.framework.domain.properties.ApplicationFrameworkProperties;
import io.tech1.framework.incidents.handlers.ErrorHandlerPublisher;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.event.ApplicationEventMulticaster;
import org.springframework.context.event.SimpleApplicationEventMulticaster;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import static io.tech1.framework.foundation.utilities.processors.ProcessorsUtility.getNumOfCores;

@Configuration
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class ApplicationEventsIncidents {

    // Exceptions
    private final ErrorHandlerPublisher errorHandlerPublisher;
    // Properties
    private final ApplicationFrameworkProperties applicationFrameworkProperties;

    @PostConstruct
    public void init() {
        this.applicationFrameworkProperties.getEventsConfigs().assertProperties(new PropertyId("eventsConfigs"));
    }

    @Bean(name = "applicationEventMulticaster")
    public ApplicationEventMulticaster simpleApplicationEventMulticaster() {
        var eventsConfigs = this.applicationFrameworkProperties.getEventsConfigs();
        var taskExecutor = new ThreadPoolTaskExecutor();
        taskExecutor.setThreadNamePrefix(eventsConfigs.getThreadNamePrefix());
        taskExecutor.setCorePoolSize(getNumOfCores(eventsConfigs.asThreadsCorePoolTuplePercentage()));
        taskExecutor.setMaxPoolSize(getNumOfCores(eventsConfigs.asThreadsMaxPoolTuplePercentage()));
        taskExecutor.initialize();
        var eventMulticaster = new SimpleApplicationEventMulticaster();
        eventMulticaster.setTaskExecutor(taskExecutor);
        eventMulticaster.setErrorHandler(this.errorHandlerPublisher);
        return eventMulticaster;
    }
}
