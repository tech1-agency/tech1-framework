package jbst.foundation.incidents.handlers;

import jbst.foundation.incidents.events.publishers.IncidentPublisher;
import lombok.RequiredArgsConstructor;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.ErrorHandler;

@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class ErrorHandlerPublisher implements ErrorHandler {

    // Publisher
    private final IncidentPublisher incidentPublisher;

    @Override
    public void handleError(@NotNull Throwable throwable) {
        this.incidentPublisher.publishThrowable(throwable);
    }
}
