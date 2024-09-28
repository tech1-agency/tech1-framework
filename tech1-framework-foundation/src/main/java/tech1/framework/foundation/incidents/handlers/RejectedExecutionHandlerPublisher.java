package tech1.framework.foundation.incidents.handlers;

import tech1.framework.foundation.incidents.events.publishers.IncidentPublisher;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.concurrent.RejectedExecutionException;
import java.util.concurrent.RejectedExecutionHandler;
import java.util.concurrent.ThreadPoolExecutor;

@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class RejectedExecutionHandlerPublisher implements RejectedExecutionHandler {

    // Publisher
    private final IncidentPublisher incidentPublisher;

    @Override
    public void rejectedExecution(Runnable runnable, ThreadPoolExecutor executor) {
        var message = "Task " + runnable.toString() + " rejected from " + executor.toString();
        var rejectedExecutionException = new RejectedExecutionException(message);
        this.incidentPublisher.publishThrowable(rejectedExecutionException);
        throw rejectedExecutionException;
    }
}
