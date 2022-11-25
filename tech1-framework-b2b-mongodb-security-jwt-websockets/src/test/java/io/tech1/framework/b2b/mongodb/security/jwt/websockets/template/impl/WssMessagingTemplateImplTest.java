package io.tech1.framework.b2b.mongodb.security.jwt.websockets.template.impl;

import io.tech1.framework.b2b.mongodb.security.jwt.websockets.domain.events.WebsocketEvent;
import io.tech1.framework.b2b.mongodb.security.jwt.websockets.template.WssMessagingTemplate;
import io.tech1.framework.incidents.domain.throwable.ThrowableIncident;
import io.tech1.framework.incidents.events.publishers.IncidentPublisher;
import io.tech1.framework.properties.ApplicationFrameworkProperties;
import io.tech1.framework.properties.tests.contexts.ApplicationFrameworkPropertiesContext;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.messaging.MessagingException;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.context.support.AnnotationConfigContextLoader;

import static io.tech1.framework.domain.utilities.random.RandomUtility.randomString;
import static io.tech1.framework.domain.utilities.random.RandomUtility.randomUsername;
import static org.mockito.Mockito.*;

@ExtendWith({ SpringExtension.class, MockitoExtension.class })
@ContextConfiguration(loader= AnnotationConfigContextLoader.class)
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class WssMessagingTemplateImplTest {

    @Configuration
    @Import({
            ApplicationFrameworkPropertiesContext.class
    })
    @RequiredArgsConstructor(onConstructor = @__(@Autowired))
    static class ContextConfiguration {
        private final ApplicationFrameworkProperties applicationFrameworkProperties;

        @Bean
        SimpMessagingTemplate simpMessagingTemplate() {
            return mock(SimpMessagingTemplate.class);
        }

        @Bean
        IncidentPublisher serverIncidentPublisher() {
            return mock(IncidentPublisher.class);
        }

        @Bean
        WssMessagingTemplate wssMessagingTemplate() {
            return new WssMessagingTemplateImpl(
                    this.simpMessagingTemplate(),
                    this.serverIncidentPublisher(),
                    this.applicationFrameworkProperties
            );
        }
    }

    private final SimpMessagingTemplate simpMessagingTemplate;
    private final IncidentPublisher incidentPublisher;
    private final ApplicationFrameworkProperties applicationFrameworkProperties;

    private final WssMessagingTemplate componentUnderTest;

    @BeforeEach
    public void beforeEach() {
        reset(
                this.simpMessagingTemplate,
                this.incidentPublisher
        );
    }

    @AfterEach
    public void afterEach() {
        verifyNoMoreInteractions(
                this.simpMessagingTemplate,
                this.incidentPublisher
        );
    }

    @Test
    public void convertAndSendToUserThrowExceptionTest() {
        // Assert
        var username = randomUsername();
        var destination = randomString();
        var websocketEvent = mock(WebsocketEvent.class);
        var ex = new MessagingException(randomString());
        var simpleDestination = this.applicationFrameworkProperties.getSecurityJwtWebsocketsConfigs().getBrokerConfigs().getSimpleDestination();
        doThrow(ex).when(this.simpMessagingTemplate).convertAndSendToUser(eq(username.getIdentifier()), eq(simpleDestination + destination), eq(websocketEvent));

        // Act
        this.componentUnderTest.sendEventToUser(username, destination, websocketEvent);

        // Assert
        verify(this.simpMessagingTemplate).convertAndSendToUser(eq(username.getIdentifier()), eq(simpleDestination + destination), eq(websocketEvent));
        verify(this.incidentPublisher).publishThrowable(eq(ThrowableIncident.of(ex)));
        verifyNoMoreInteractions(this.simpMessagingTemplate);
    }

    @Test
    public void convertAndSendToUserTest() {
        // Assert
        var username = randomUsername();
        var destination = randomString();
        var websocketEvent = mock(WebsocketEvent.class);
        var simpleDestination = this.applicationFrameworkProperties.getSecurityJwtWebsocketsConfigs().getBrokerConfigs().getSimpleDestination();

        // Act
        this.componentUnderTest.sendEventToUser(username, destination, websocketEvent);

        // Assert
        verify(this.simpMessagingTemplate).convertAndSendToUser(eq(username.getIdentifier()), eq(simpleDestination + destination), eq(websocketEvent));
        verifyNoMoreInteractions(this.simpMessagingTemplate);
    }
}
