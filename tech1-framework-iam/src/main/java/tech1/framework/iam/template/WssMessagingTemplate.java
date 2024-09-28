package tech1.framework.iam.template;

import tech1.framework.foundation.domain.base.Username;
import tech1.framework.foundation.domain.hardware.monitoring.HardwareMonitoringDatapointTableView;
import tech1.framework.foundation.domain.system.reset_server.ResetServerStatus;
import tech1.framework.iam.domain.events.WebsocketEvent;
import org.springframework.scheduling.annotation.Async;

import java.util.Set;

public interface WssMessagingTemplate {
    @Async
    void sendEventToUser(Username username, String destination, WebsocketEvent event);

    @Async
    void sendHardwareMonitoring(Set<Username> usernames, HardwareMonitoringDatapointTableView tableView);
    @Async
    void sendResetServerStatus(Set<Username> usernames, ResetServerStatus status);
}
