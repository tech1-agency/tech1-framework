package jbst.foundation.services.hardware.publishers.impl;

import jbst.foundation.domain.events.hardware.EventLastHardwareMonitoringDatapoint;
import jbst.foundation.domain.pubsub.AbstractEventPublisher;
import jbst.foundation.services.hardware.publishers.HardwareMonitoringPublisher;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;

@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class HardwareMonitoringPublisherImpl extends AbstractEventPublisher implements HardwareMonitoringPublisher {

    // Spring Publisher
    private final ApplicationEventPublisher applicationEventPublisher;

    @Override
    public void publishLastHardwareMonitoringDatapoint(EventLastHardwareMonitoringDatapoint event) {
        this.applicationEventPublisher.publishEvent(event);
    }
}
