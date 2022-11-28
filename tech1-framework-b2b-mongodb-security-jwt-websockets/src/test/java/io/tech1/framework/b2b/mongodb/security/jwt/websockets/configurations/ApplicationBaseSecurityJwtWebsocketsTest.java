package io.tech1.framework.b2b.mongodb.security.jwt.websockets.configurations;

import io.tech1.framework.b2b.mongodb.security.jwt.websockets.handshakes.CsrfInterceptorHandshake;
import io.tech1.framework.b2b.mongodb.security.jwt.websockets.handshakes.SecurityHandshakeHandler;
import io.tech1.framework.b2b.mongodb.security.jwt.websockets.template.WssMessagingTemplate;
import io.tech1.framework.domain.properties.configs.MvcConfigs;
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

import static io.tech1.framework.domain.tests.constants.TestsPropertiesConstants.SECURITY_JWT_WEBSOCKETS_CONFIGS;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@ExtendWith({ SpringExtension.class })
@ContextConfiguration(loader= AnnotationConfigContextLoader.class)
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class ApplicationBaseSecurityJwtWebsocketsTest {

    @Configuration
    @Import({
            ApplicationBaseSecurityJwtWebsockets.class
    })
    static class ContextConfiguration {
        @Bean
        public ApplicationFrameworkProperties applicationFrameworkProperties() {
            var properties = new ApplicationFrameworkProperties();
            properties.setMvcConfigs(
                    MvcConfigs.of(
                            true,
                            "/framework/security",
                            CorsConfigs.of(
                                    "/api/**",
                                    new String[] { "http://localhost:1234" },
                                    new String[] { "GET", "POST" },
                                    new String[] { "Access-Control-Allow-Origin" },
                                    true,
                                    null
                            )
                    )
            );
            properties.setSecurityJwtWebsocketsConfigs(SECURITY_JWT_WEBSOCKETS_CONFIGS);
            return properties;
        }

        // @Configuration
        @Bean
        CsrfInterceptorHandshake csrfInterceptorHandshake() {
            return mock(CsrfInterceptorHandshake.class);
        }

        @Bean
        SecurityHandshakeHandler securityHandshakeHandler() {
            return mock(SecurityHandshakeHandler.class);
        }

        // @Beans
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
    public void beansTests() {
        // Act
        var methods = Stream.of(this.componentUnderTest.getClass().getMethods())
                .map(Method::getName)
                .collect(Collectors.toList());

        // Assert
        assertThat(methods).contains("registerStompEndpoints");
        assertThat(methods).contains("configureMessageBroker");
        assertThat(methods).hasSize(31);
    }

    @Test
    public void registerStompEndpointsTest() {
        // Arrange
        var registration = mock(StompWebSocketEndpointRegistration.class);
        var registry = mock(StompEndpointRegistry.class);
        when(registration.setAllowedOrigins(eq("http://localhost:1234"))).thenReturn(registration);
        when(registration.setHandshakeHandler(eq(this.securityHandshakeHandler))).thenReturn(registration);
        when(registration.addInterceptors(eq(this.csrfInterceptorHandshake))).thenReturn(registration);
        when(registry.addEndpoint(eq("/endpoint"))).thenReturn(registration);

        // Act
        this.componentUnderTest.registerStompEndpoints(registry);

        // Assert
        verify(registry).addEndpoint(eq("/endpoint"));
        verify(registration).setAllowedOrigins(eq("http://localhost:1234"));
        verify(registration).setHandshakeHandler(eq(this.securityHandshakeHandler));
        verify(registration).addInterceptors(eq(this.csrfInterceptorHandshake));
        verify(registration).withSockJS();
        verifyNoMoreInteractions(
                registry,
                registration
        );
    }

    @Test
    public void configureMessageBrokerTest() {
        // Arrange
        var registry = mock(MessageBrokerRegistry.class);

        // Act
        this.componentUnderTest.configureMessageBroker(registry);

        // Assert
        verify(registry).setApplicationDestinationPrefixes(eq("/app"));
        verify(registry).enableSimpleBroker(eq("/queue"));
        verify(registry).setUserDestinationPrefix(eq("/user"));
        verifyNoMoreInteractions(
                registry
        );
    }

    @Test
    public void sameOriginDisabledTest() {
        // Act
        var actual = this.componentUnderTest.sameOriginDisabled();

        // Assert
        assertThat(actual).isFalse();
    }
}
