package io.tech1.framework.b2b.base.security.jwt.websockets.configurations;

import io.tech1.framework.b2b.base.security.jwt.websockets.handshakes.CsrfInterceptorHandshake;
import io.tech1.framework.b2b.base.security.jwt.websockets.handshakes.SecurityHandshakeHandler;
import io.tech1.framework.b2b.base.security.jwt.websockets.tempate.WssMessagingTemplate;
import io.tech1.framework.domain.properties.configs.MvcConfigs;
import io.tech1.framework.domain.properties.configs.SecurityJwtWebsocketsConfigs;
import io.tech1.framework.domain.properties.configs.mvc.CorsConfigs;
import io.tech1.framework.properties.ApplicationFrameworkProperties;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.context.support.AnnotationConfigContextLoader;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.config.annotation.StompWebSocketEndpointRegistration;

import java.lang.reflect.Method;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

@ExtendWith({ SpringExtension.class })
@ContextConfiguration(loader= AnnotationConfigContextLoader.class)
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
class ApplicationBaseSecurityJwtWebsocketsTest {

    @Configuration
    @Import({
            ApplicationBaseSecurityJwtWebsockets.class
    })
    static class ContextConfiguration {
        @Bean
        public ApplicationFrameworkProperties applicationFrameworkProperties() {
            var properties = new ApplicationFrameworkProperties();
            properties.setMvcConfigs(
                    new MvcConfigs(
                            true,
                            "/framework/security",
                            new CorsConfigs(
                                    "/api/**",
                                    new String[] { "http://localhost:1234" },
                                    new String[] { "GET", "POST" },
                                    new String[] { "Access-Control-Allow-Origin" },
                                    true,
                                    null
                            )
                    )
            );
            properties.setSecurityJwtWebsocketsConfigs(SecurityJwtWebsocketsConfigs.testsHardcoded());
            return properties;
        }

        @Bean
        CsrfInterceptorHandshake csrfInterceptorHandshake() {
            return mock(CsrfInterceptorHandshake.class);
        }

        @Bean
        SecurityHandshakeHandler securityHandshakeHandler() {
            return mock(SecurityHandshakeHandler.class);
        }

        @Bean
        SimpMessagingTemplate messagingTemplate() {
            return mock(SimpMessagingTemplate.class);
        }

        @Bean
        WssMessagingTemplate wssMessagingTemplate() {
            return mock(WssMessagingTemplate.class);
        }
    }

    // Handshakes
    private final CsrfInterceptorHandshake csrfInterceptorHandshake;
    private final SecurityHandshakeHandler securityHandshakeHandler;

    private final ApplicationBaseSecurityJwtWebsockets componentUnderTest;

    @Test
    void beansTests() {
        // Act
        var methods = Stream.of(this.componentUnderTest.getClass().getMethods())
                .map(Method::getName)
                .collect(Collectors.toList());

        // Assert
        assertThat(methods)
                .hasSize(31)
                .contains("registerStompEndpoints")
                .contains("configureMessageBroker");
    }

    @Test
    void registerStompEndpointsTest() {
        // Arrange
        var registration = mock(StompWebSocketEndpointRegistration.class);
        var registry = mock(StompEndpointRegistry.class);
        when(registration.setAllowedOrigins("http://localhost:1234")).thenReturn(registration);
        when(registration.setHandshakeHandler(this.securityHandshakeHandler)).thenReturn(registration);
        when(registration.addInterceptors(this.csrfInterceptorHandshake)).thenReturn(registration);
        when(registry.addEndpoint("/endpoint")).thenReturn(registration);

        // Act
        this.componentUnderTest.registerStompEndpoints(registry);

        // Assert
        verify(registry).addEndpoint("/endpoint");
        verify(registration).setAllowedOrigins("http://localhost:1234");
        verify(registration).setHandshakeHandler(this.securityHandshakeHandler);
        verify(registration).addInterceptors(this.csrfInterceptorHandshake);
        verify(registration).withSockJS();
        verifyNoMoreInteractions(
                registry,
                registration
        );
    }

    @Test
    void configureMessageBrokerTest() {
        // Arrange
        var registry = mock(MessageBrokerRegistry.class);

        // Act
        this.componentUnderTest.configureMessageBroker(registry);

        // Assert
        verify(registry).setApplicationDestinationPrefixes("/app");
        verify(registry).enableSimpleBroker("/queue");
        verify(registry).setUserDestinationPrefix("/user");
        verifyNoMoreInteractions(
                registry
        );
    }

    @Test
    void sameOriginDisabledTest() {
        // Act
        var actual = this.componentUnderTest.sameOriginDisabled();

        // Assert
        assertThat(actual).isFalse();
    }
}
