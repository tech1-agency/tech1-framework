package io.tech1.framework.foundation.domain.pubsub;

public abstract class AbstractEventPublisher implements AbstractEventProcessor {

    @Override
    public EventProcessorType getType() {
        return EventProcessorType.PUBLISHER;
    }
}
