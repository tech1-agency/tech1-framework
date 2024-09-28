package tech1.framework.foundation.incidents.handlers;

import tech1.framework.foundation.incidents.domain.Incident;
import tech1.framework.foundation.incidents.events.publishers.IncidentPublisher;
import lombok.RequiredArgsConstructor;
import org.jetbrains.annotations.NotNull;
import org.springframework.aop.interceptor.AsyncUncaughtExceptionHandler;
import org.springframework.beans.factory.annotation.Autowired;

import java.lang.reflect.Method;
import java.util.Arrays;

@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class AsyncUncaughtExceptionHandlerPublisher implements AsyncUncaughtExceptionHandler {

    // Publisher
    private final IncidentPublisher incidentPublisher;

    @Override
    public void handleUncaughtException(@NotNull Throwable throwable, @NotNull Method method, Object @NotNull ... params) {
        this.incidentPublisher.publishIncident(
                new Incident(
                        throwable,
                        method,
                        Arrays.asList(params)
                )
        );
    }
}
