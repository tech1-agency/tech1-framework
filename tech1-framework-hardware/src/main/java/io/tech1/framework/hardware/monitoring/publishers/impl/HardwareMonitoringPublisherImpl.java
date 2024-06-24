package io.tech1.framework.hardware.monitoring.publishers.impl;

import io.tech1.framework.foundation.domain.events.hardware.EventLastHardwareMonitoringDatapoint;
import io.tech1.framework.foundation.domain.pubsub.AbstractEventPublisher;
import io.tech1.framework.hardware.monitoring.publishers.HardwareMonitoringPublisher;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class HardwareMonitoringPublisherImpl extends AbstractEventPublisher implements HardwareMonitoringPublisher {

    // Spring Publisher
    private final ApplicationEventPublisher applicationEventPublisher;

    @Override
    public void publishLastHardwareMonitoringDatapoint(EventLastHardwareMonitoringDatapoint event) {
        this.applicationEventPublisher.publishEvent(event);
    }
}
