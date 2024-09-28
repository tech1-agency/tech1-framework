package tech1.framework.foundation.services.hardware.subscribers.base;

import tech1.framework.foundation.domain.events.hardware.EventLastHardwareMonitoringDatapoint;
import tech1.framework.foundation.domain.pubsub.AbstractEventSubscriber;
import tech1.framework.foundation.services.hardware.store.HardwareMonitoringStore;
import tech1.framework.foundation.services.hardware.subscribers.HardwareMonitoringSubscriber;
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
