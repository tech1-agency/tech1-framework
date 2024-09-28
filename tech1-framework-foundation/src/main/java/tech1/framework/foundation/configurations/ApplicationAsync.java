package tech1.framework.foundation.configurations;

import tech1.framework.foundation.domain.base.PropertyId;
import tech1.framework.foundation.domain.properties.ApplicationFrameworkProperties;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.aop.interceptor.AsyncUncaughtExceptionHandler;
import org.springframework.aop.interceptor.SimpleAsyncUncaughtExceptionHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.AsyncConfigurer;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import java.util.concurrent.Executor;

import static tech1.framework.foundation.utilities.processors.ProcessorsUtility.getNumOfCores;

@Configuration
@EnableAsync
@EnableScheduling
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class ApplicationAsync implements AsyncConfigurer {

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
        taskExecutor.initialize();
        return taskExecutor;
    }

    @Override
    public AsyncUncaughtExceptionHandler getAsyncUncaughtExceptionHandler() {
        return new SimpleAsyncUncaughtExceptionHandler();
    }
}
