package jbst.iam.template.impl;

import tech1.framework.foundation.domain.base.Username;
import tech1.framework.foundation.domain.hardware.monitoring.HardwareMonitoringDatapointTableView;
import tech1.framework.foundation.domain.properties.ApplicationFrameworkProperties;
import tech1.framework.foundation.domain.system.reset_server.ResetServerStatus;
import tech1.framework.foundation.incidents.events.publishers.IncidentPublisher;
import jbst.iam.domain.events.WebsocketEvent;
import jbst.iam.template.WssMessagingTemplate;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.MessagingException;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Component;

import java.util.Set;

import static jbst.iam.domain.events.WebsocketEvent.hardwareMonitoring;
import static jbst.iam.domain.events.WebsocketEvent.resetServerProgress;

@Component
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class WssMessagingTemplateImpl implements WssMessagingTemplate {

    // WebSocket - Stomp
    private final SimpMessagingTemplate messagingTemplate;
    // Incidents
    private final IncidentPublisher incidentPublisher;
    // Properties
    private final ApplicationFrameworkProperties applicationFrameworkProperties;

    @Override
    public void sendEventToUser(Username username, String destination, WebsocketEvent event) {
        this.sendObjectToUser(
                username,
                destination,
                event
        );
    }

    @Override
    public void sendHardwareMonitoring(Set<Username> usernames, HardwareMonitoringDatapointTableView tableView) {
        var hardwareConfigs = this.applicationFrameworkProperties.getSecurityJwtWebsocketsConfigs().getFeaturesConfigs().getHardwareConfigs();
        if (!hardwareConfigs.isEnabled()) {
            return;
        }
        usernames.forEach(username -> this.sendObjectToUser(username, hardwareConfigs.getUserDestination(), hardwareMonitoring(tableView)));
    }

    @Override
    public void sendResetServerStatus(Set<Username> usernames, ResetServerStatus status) {
        var resetServerConfigs = this.applicationFrameworkProperties.getSecurityJwtWebsocketsConfigs().getFeaturesConfigs().getResetServerConfigs();
        if (!resetServerConfigs.isEnabled()) {
            return;
        }
        usernames.forEach(username -> this.sendObjectToUser(username, resetServerConfigs.getUserDestination(), resetServerProgress(status)));
    }

    // =================================================================================================================
    // PRIVATE METHODS
    // =================================================================================================================
    private void sendObjectToUser(Username username, String destination, Object data) {
        if (!this.applicationFrameworkProperties.getSecurityJwtWebsocketsConfigs().getTemplateConfigs().isEnabled()) {
            return;
        }
        var brokerConfigs = this.applicationFrameworkProperties.getSecurityJwtWebsocketsConfigs().getBrokerConfigs();
        try {
            this.messagingTemplate.convertAndSendToUser(
                    username.value(),
                    brokerConfigs.getSimpleDestination() + destination,
                    data
            );
        } catch (MessagingException ex) {
            this.incidentPublisher.publishThrowable(ex);
        }
    }
}
