package io.tech1.framework.incidents.configurations;

import io.tech1.framework.foundation.domain.base.PropertyId;
import io.tech1.framework.foundation.domain.properties.ApplicationFrameworkProperties;
import io.tech1.framework.incidents.handlers.AsyncUncaughtExceptionHandlerPublisher;
import io.tech1.framework.incidents.handlers.RejectedExecutionHandlerPublisher;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.aop.interceptor.AsyncUncaughtExceptionHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.AsyncConfigurer;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import java.util.concurrent.Executor;

import static io.tech1.framework.foundation.utilities.processors.ProcessorsUtility.getNumOfCores;

@Configuration
@EnableAsync
@EnableScheduling
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class ApplicationAsyncIncidents implements AsyncConfigurer {

    // Exceptions
    private final AsyncUncaughtExceptionHandlerPublisher asyncUncaughtExceptionHandlerPublisher;
    private final RejectedExecutionHandlerPublisher rejectedExecutionHandlerPublisher;
    // Properties
    private final ApplicationFrameworkProperties applicationFrameworkProperties;

    @PostConstruct
    public void init() {
        this.applicationFrameworkProperties.getAsyncConfigs().assertProperties(new PropertyId("asyncConfigs"));
    }

    @Override
    public Executor getAsyncExecutor() {
        var asyncConfigs = this.applicationFrameworkProperties.getAsyncConfigs();
        var taskExecutor = new ThreadPoolTaskExecutor();
        taskExecutor.setThreadNamePrefix(asyncConfigs.getThreadNamePrefix());
        taskExecutor.setCorePoolSize(getNumOfCores(asyncConfigs.asThreadsCorePoolTuplePercentage()));
        taskExecutor.setMaxPoolSize(getNumOfCores(asyncConfigs.asThreadsMaxPoolTuplePercentage()));
        taskExecutor.setRejectedExecutionHandler(this.rejectedExecutionHandlerPublisher);
        taskExecutor.initialize();
        return taskExecutor;
    }

    @Override
    public AsyncUncaughtExceptionHandler getAsyncUncaughtExceptionHandler() {
        return this.asyncUncaughtExceptionHandlerPublisher;
    }
}
