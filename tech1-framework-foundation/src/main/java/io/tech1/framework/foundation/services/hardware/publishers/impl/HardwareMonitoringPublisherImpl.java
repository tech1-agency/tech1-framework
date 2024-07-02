package io.tech1.framework.foundation.services.hardware.publishers.impl;

import io.tech1.framework.foundation.domain.events.hardware.EventLastHardwareMonitoringDatapoint;
import io.tech1.framework.foundation.domain.pubsub.AbstractEventPublisher;
import io.tech1.framework.foundation.services.hardware.publishers.HardwareMonitoringPublisher;
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
