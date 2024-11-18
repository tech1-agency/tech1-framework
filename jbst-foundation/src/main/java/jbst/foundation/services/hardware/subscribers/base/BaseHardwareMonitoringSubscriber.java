package jbst.foundation.services.hardware.subscribers.base;

import jbst.foundation.domain.events.hardware.EventLastHardwareMonitoringDatapoint;
import jbst.foundation.domain.pubsub.AbstractEventSubscriber;
import jbst.foundation.services.hardware.store.HardwareMonitoringStore;
import jbst.foundation.services.hardware.subscribers.HardwareMonitoringSubscriber;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class BaseHardwareMonitoringSubscriber extends AbstractEventSubscriber implements HardwareMonitoringSubscriber {

    private final HardwareMonitoringStore hardwareMonitoringStore;

    @Override
    public void onLastHardwareMonitoringDatapoint(EventLastHardwareMonitoringDatapoint event) {
        this.hardwareMonitoringStore.storeEvent(event);
    }
}
