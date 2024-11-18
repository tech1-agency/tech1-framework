package jbst.foundation.domain.pubsub;

public abstract class AbstractEventSubscriber implements AbstractEventProcessor {

    @Override
    public EventProcessorType getType() {
        return EventProcessorType.SUBSCRIBER;
    }
}
