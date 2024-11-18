package jbst.iam.template.impl;

import jbst.foundation.domain.properties.configs.security.jwt.websockets.*;
import jbst.iam.domain.events.WebsocketEvent;
import jbst.iam.template.WssMessagingTemplate;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.MessagingException;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.context.support.AnnotationConfigContextLoader;
import jbst.foundation.domain.base.Username;
import jbst.foundation.domain.properties.JbstProperties;
import jbst.foundation.domain.properties.configs.SecurityJwtWebsocketsConfigs;
import jbst.foundation.incidents.events.publishers.IncidentPublisher;

import java.util.stream.Stream;

import static org.mockito.Mockito.*;
import static jbst.foundation.utilities.random.RandomUtility.randomString;

@ExtendWith({ SpringExtension.class, MockitoExtension.class })
@ContextConfiguration(loader= AnnotationConfigContextLoader.class)
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
class WssMessagingTemplateImplTest {

    private static Stream<Arguments> convertAndSendToUserTestArgs() {
        return Stream.of(
                Arguments.of(WebsocketsTemplateConfigs.enabled(), true),
                Arguments.of(WebsocketsTemplateConfigs.disabled(), false)
        );
    }

    @Configuration
    @RequiredArgsConstructor(onConstructor = @__(@Autowired))
    static class ContextConfiguration {
        @Bean
        JbstProperties applicationFrameworkProperties() {
            return mock(JbstProperties.class);
        }

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
                    this.applicationFrameworkProperties()
            );
        }
    }

    private final SimpMessagingTemplate simpMessagingTemplate;
    private final IncidentPublisher incidentPublisher;
    private final JbstProperties jbstProperties;

    private final WssMessagingTemplate componentUnderTest;

    @BeforeEach
    void beforeEach() {
        reset(
                this.simpMessagingTemplate,
                this.incidentPublisher,
                this.jbstProperties
        );
    }

    @AfterEach
    void afterEach() {
        verifyNoMoreInteractions(
                this.simpMessagingTemplate,
                this.incidentPublisher,
                this.jbstProperties
        );
    }

    @Test
    void convertAndSendToUserThrowExceptionTest() {
        // Assert
        when(this.jbstProperties.getSecurityJwtWebsocketsConfigs()).thenReturn(SecurityJwtWebsocketsConfigs.hardcoded());
        var username = Username.random();
        var websocketEvent = mock(WebsocketEvent.class);
        var ex = new MessagingException(randomString());
        var destination = "/" + randomString();
        doThrow(ex).when(this.simpMessagingTemplate).convertAndSendToUser(username.value(), "/queue" + destination, websocketEvent);

        // Act
        this.componentUnderTest.sendEventToUser(username, destination, websocketEvent);

        // Assert
        verify(this.jbstProperties, times(2)).getSecurityJwtWebsocketsConfigs();
        verify(this.simpMessagingTemplate).convertAndSendToUser(username.value(), "/queue" + destination, websocketEvent);
        verify(this.incidentPublisher).publishThrowable(ex);
        verifyNoMoreInteractions(this.simpMessagingTemplate);
    }

    @ParameterizedTest
    @MethodSource("convertAndSendToUserTestArgs")
    void convertAndSendToUserTest(WebsocketsTemplateConfigs configs, boolean expectedSend) {
        // Assert
        when(this.jbstProperties.getSecurityJwtWebsocketsConfigs()).thenReturn(
                new SecurityJwtWebsocketsConfigs(
                        CsrfConfigs.hardcoded(),
                        StompEndpointRegistryConfigs.hardcoded(),
                        MessageBrokerRegistryConfigs.hardcoded(),
                        configs,
                        WebsocketsFeaturesConfigs.hardcoded()
                )
        );
        var username = Username.random();
        var destination = randomString();
        var websocketEvent = mock(WebsocketEvent.class);

        // Act
        this.componentUnderTest.sendEventToUser(username, destination, websocketEvent);

        // Assert
        if (expectedSend) {
            verify(this.jbstProperties, times(2)).getSecurityJwtWebsocketsConfigs();
            verify(this.simpMessagingTemplate).convertAndSendToUser(username.value(), "/queue" + destination, websocketEvent);
            verifyNoMoreInteractions(this.simpMessagingTemplate);
        } else {
            verify(this.jbstProperties).getSecurityJwtWebsocketsConfigs();
        }
    }
}
