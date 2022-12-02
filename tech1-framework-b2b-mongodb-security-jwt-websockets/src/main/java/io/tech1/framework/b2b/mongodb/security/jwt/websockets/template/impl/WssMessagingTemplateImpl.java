package io.tech1.framework.b2b.mongodb.security.jwt.websockets.template.impl;

import io.tech1.framework.b2b.mongodb.security.jwt.websockets.domain.events.WebsocketEvent;
import io.tech1.framework.b2b.mongodb.security.jwt.websockets.template.WssMessagingTemplate;
import io.tech1.framework.domain.base.Username;
import io.tech1.framework.incidents.domain.throwable.IncidentThrowable;
import io.tech1.framework.incidents.events.publishers.IncidentPublisher;
import io.tech1.framework.properties.ApplicationFrameworkProperties;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.MessagingException;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Component;

@Slf4j
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

    // =================================================================================================================
    // PRIVATE METHODS
    // =================================================================================================================
    private void sendObjectToUser(Username username, String destination, Object data) {
        var brokerConfigs = this.applicationFrameworkProperties.getSecurityJwtWebsocketsConfigs().getBrokerConfigs();
        try {
            this.messagingTemplate.convertAndSendToUser(
                    username.getIdentifier(),
                    brokerConfigs.getSimpleDestination() + destination,
                    data
            );
        } catch (MessagingException ex) {
            this.incidentPublisher.publishThrowable(IncidentThrowable.of(ex));
        }
    }
}
