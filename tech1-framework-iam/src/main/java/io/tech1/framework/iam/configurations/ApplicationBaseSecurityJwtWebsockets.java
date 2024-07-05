package io.tech1.framework.iam.configurations;

import io.tech1.framework.foundation.domain.base.PropertyId;
import io.tech1.framework.foundation.domain.properties.ApplicationFrameworkProperties;
import io.tech1.framework.foundation.incidents.events.publishers.IncidentPublisher;
import io.tech1.framework.foundation.services.hardware.store.HardwareMonitoringStore;
import io.tech1.framework.iam.events.subscribers.websockets.HardwareMonitoringSubscriberWebsockets;
import io.tech1.framework.iam.handshakes.CsrfInterceptorHandshake;
import io.tech1.framework.iam.handshakes.SecurityHandshakeHandler;
import io.tech1.framework.iam.services.TokensService;
import io.tech1.framework.iam.sessions.SessionRegistry;
import io.tech1.framework.iam.tasks.HardwareBackPressureTimerTask;
import io.tech1.framework.iam.template.WssMessagingTemplate;
import io.tech1.framework.iam.template.impl.WssMessagingTemplateImpl;
import io.tech1.framework.iam.tokens.facade.TokensProvider;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.context.annotation.Primary;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.security.config.annotation.web.messaging.MessageSecurityMetadataSourceRegistry;
import org.springframework.security.config.annotation.web.socket.AbstractSecurityWebSocketMessageBrokerConfigurer;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;

/**
 * <a href="https://docs.spring.io/spring-security/reference/servlet/integrations/websocket.html">Documentation #1</a>
 * <a href="https://docs.spring.io/spring-framework/docs/current/reference/html/web.html#websocket">Documentation #2</a>
 * <p>
 * Spring Boot 3 Migration Issues 24.04.2024:
 * <p>
 * <a href="https://github.com/spring-projects/spring-security/issues/13640">
 * EnableWebSocketSecurity is not 1:1 replacement for AbstractSecurityWebSocketMessageBrokerConfigurer
 * </a>
 * <p>
 * <a href="https://github.com/jhipster/generator-jhipster/issues/20404">
 * Migrate to Spring Security 6's @EnableWebSocketSecurity (it is not possible to disable CSRF currently)
 * </a>
 */
// idea - reconnect flow: https://stackoverflow.com/questions/53244720/spring-websocket-stomp-exception-handling
@Configuration
@Import({
        ApplicationBaseSecurityJwt.class
})
@EnableWebSocketMessageBroker
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class ApplicationBaseSecurityJwtWebsockets extends AbstractSecurityWebSocketMessageBrokerConfigurer {

    // Handshakes
    private final CsrfInterceptorHandshake csrfInterceptorHandshake;
    private final SecurityHandshakeHandler securityHandshakeHandler;
    // Properties
    private final ApplicationFrameworkProperties applicationFrameworkProperties;

    @PostConstruct
    public void init() {
        this.applicationFrameworkProperties.getSecurityJwtWebsocketsConfigs().assertProperties(new PropertyId("securityJwtWebsocketsConfigs"));
    }

    // =================================================================================================================
    // @Overrides
    // =================================================================================================================
    @Override
    public void registerStompEndpoints(StompEndpointRegistry registry) {
        registry.addEndpoint(this.applicationFrameworkProperties.getSecurityJwtWebsocketsConfigs().getStompConfigs().getEndpoint())
                .setAllowedOrigins(this.applicationFrameworkProperties.getMvcConfigs().getCorsConfigs().getAllowedOrigins())
                .setHandshakeHandler(this.securityHandshakeHandler)
                .addInterceptors(this.csrfInterceptorHandshake)
                .withSockJS();
    }

    @Override
    protected void configureInbound(MessageSecurityMetadataSourceRegistry registry) {
        registry.anyMessage().authenticated();
    }

    @Override
    public void configureMessageBroker(MessageBrokerRegistry registry) {
        var broker = this.applicationFrameworkProperties.getSecurityJwtWebsocketsConfigs().getBrokerConfigs();
        registry.setApplicationDestinationPrefixes(broker.getApplicationDestinationPrefix());
        registry.enableSimpleBroker(broker.getSimpleDestination());
        registry.setUserDestinationPrefix(broker.getUserDestinationPrefix());
    }

    /**
     * Determines if a CSRF token is required for connecting. This protects against remote
     * sites from connecting to the application and being able to read/write data over the
     * connection. The default is false (the token is required).
     */
    @Override
    protected boolean sameOriginDisabled() {
        return false;
    }

    // =================================================================================================================
    // @Beans
    // =================================================================================================================
    @Bean
    CsrfInterceptorHandshake csrfInterceptorHandshake(
            TokensProvider tokensProvider
    ) {
        return new CsrfInterceptorHandshake(tokensProvider);
    }

    @Bean
    HardwareBackPressureTimerTask hardwareBackPressureTimerTask(
            SessionRegistry sessionRegistry,
            WssMessagingTemplate wssMessagingTemplate,
            HardwareMonitoringStore hardwareMonitoringStore,
            IncidentPublisher incidentPublisher
    ) {
        return new HardwareBackPressureTimerTask(
                sessionRegistry,
                wssMessagingTemplate,
                hardwareMonitoringStore,
                incidentPublisher,
                this.applicationFrameworkProperties
        );
    }

    @Primary
    @Bean
    HardwareMonitoringSubscriberWebsockets hardwareMonitoringSubscriberWebsockets(
            HardwareMonitoringStore hardwareMonitoringStore,
            HardwareBackPressureTimerTask hardwareBackPressureTimerTask,
            IncidentPublisher incidentPublisher
    ) {
        return new HardwareMonitoringSubscriberWebsockets(
                hardwareMonitoringStore,
                hardwareBackPressureTimerTask,
                incidentPublisher
        );
    }

    @Bean
    SecurityHandshakeHandler securityHandshakeHandler(
            TokensService tokensService,
            TokensProvider tokensProvider
    ) {
        return new SecurityHandshakeHandler(
                tokensService,
                tokensProvider
        );
    }

    @Bean
    WssMessagingTemplate wssMessagingTemplate(
            SimpMessagingTemplate simpMessagingTemplate,
            IncidentPublisher incidentPublisher
    ) {
        return new WssMessagingTemplateImpl(
                simpMessagingTemplate,
                incidentPublisher,
                this.applicationFrameworkProperties
        );
    }
}
