package jbst.foundation.configurations;

import jakarta.annotation.PostConstruct;
import jbst.foundation.domain.base.PropertyId;
import jbst.foundation.domain.properties.JbstProperties;
import jbst.foundation.incidents.handlers.AsyncUncaughtExceptionHandlerPublisher;
import jbst.foundation.incidents.handlers.RejectedExecutionHandlerPublisher;
import lombok.RequiredArgsConstructor;
import org.springframework.aop.interceptor.AsyncUncaughtExceptionHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.AsyncConfigurer;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import java.util.concurrent.Executor;

import static jbst.foundation.utilities.processors.ProcessorsUtility.getNumOfCores;

@Configuration
@EnableAsync
@EnableScheduling
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class ConfigurationAsyncIncidents implements AsyncConfigurer {

    // Exceptions
    private final AsyncUncaughtExceptionHandlerPublisher asyncUncaughtExceptionHandlerPublisher;
    private final RejectedExecutionHandlerPublisher rejectedExecutionHandlerPublisher;
    // Properties
    private final JbstProperties jbstProperties;

    @PostConstruct
    public void init() {
        this.jbstProperties.getAsyncConfigs().assertProperties(new PropertyId("asyncConfigs"));
    }

    @Override
    public Executor getAsyncExecutor() {
        var asyncConfigs = this.jbstProperties.getAsyncConfigs();
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
