package io.tech1.framework.incidents.handlers;

import io.tech1.framework.incidents.domain.throwable.IncidentThrowable;
import io.tech1.framework.incidents.events.publishers.IncidentPublisher;
import lombok.RequiredArgsConstructor;
import org.springframework.aop.interceptor.AsyncUncaughtExceptionHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;
import java.util.Arrays;

@Component
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class AsyncUncaughtExceptionHandlerPublisher implements AsyncUncaughtExceptionHandler {

    // Publisher
    private final IncidentPublisher incidentPublisher;

    @Override
    public void handleUncaughtException(Throwable throwable, Method method, Object... params) {
        this.incidentPublisher.publishThrowable(
                IncidentThrowable.of(
                        throwable,
                        method,
                        Arrays.asList(params)
                )
        );
    }
}
