package io.tech1.framework.incidents.handlers;

import io.tech1.framework.incidents.events.publishers.IncidentPublisher;
import lombok.RequiredArgsConstructor;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.ErrorHandler;

@Component
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class ErrorHandlerPublisher implements ErrorHandler {

    // Publisher
    private final IncidentPublisher incidentPublisher;

    @Override
    public void handleError(@NotNull Throwable throwable) {
        this.incidentPublisher.publishThrowable(throwable);
    }
}
