package jbst.foundation.configurations;

import feign.Feign;
import feign.auth.BasicAuthRequestInterceptor;
import feign.jackson.JacksonDecoder;
import feign.jackson.JacksonEncoder;
import feign.okhttp.OkHttpClient;
import jbst.foundation.domain.base.PropertyId;
import jbst.foundation.domain.properties.ApplicationFrameworkProperties;
import jbst.foundation.incidents.events.publishers.IncidentPublisher;
import jbst.foundation.incidents.events.publishers.impl.IncidentPublisherImpl;
import jbst.foundation.incidents.events.subscribers.IncidentSubscriber;
import jbst.foundation.incidents.events.subscribers.impl.IncidentSubscriberImpl;
import jbst.foundation.incidents.feigns.clients.IncidentClient;
import jbst.foundation.incidents.feigns.clients.impl.IncidentClientImpl;
import jbst.foundation.incidents.feigns.definitions.IncidentClientDefinition;
import jbst.foundation.incidents.feigns.definitions.IncidentClientDefinitionSlf4j;
import jbst.foundation.incidents.handlers.AsyncUncaughtExceptionHandlerPublisher;
import jbst.foundation.incidents.handlers.ErrorHandlerPublisher;
import jbst.foundation.incidents.handlers.RejectedExecutionHandlerPublisher;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.aop.interceptor.AsyncUncaughtExceptionHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.util.ErrorHandler;

import java.util.concurrent.RejectedExecutionHandler;

@Configuration
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class ApplicationIncidents {

    // Spring Publisher
    private final ApplicationEventPublisher applicationEventPublisher;
    // Properties
    private final ApplicationFrameworkProperties applicationFrameworkProperties;

    @PostConstruct
    public void init() {
        this.applicationFrameworkProperties.getIncidentConfigs().assertProperties(new PropertyId("incidentConfigs"));
    }

    @Bean
    @ConditionalOnProperty(value = "tech1.incident-configs.enabled", havingValue = "true")
    IncidentClientDefinition incidentClientDefinition() {
        var incidentServer = this.applicationFrameworkProperties.getIncidentConfigs().getRemoteServer();
        return Feign.builder()
                .client(new OkHttpClient())
                .encoder(new JacksonEncoder())
                .decoder(new JacksonDecoder())
                .requestInterceptor(
                        new BasicAuthRequestInterceptor(
                                incidentServer.getCredentials().username().value(),
                                incidentServer.getCredentials().password().value()
                        )
                )
                .target(IncidentClientDefinition.class, incidentServer.getBaseURL());
    }

    @Bean
    @ConditionalOnProperty(value = "tech1.incident-configs.enabled", havingValue = "false", matchIfMissing = true)
    IncidentClientDefinition incidentClientDefinitionSlf4j() {
        return new IncidentClientDefinitionSlf4j();
    }

    @Bean
    IncidentClient incidentClient(IncidentClientDefinition incidentClientDefinition) {
        return new IncidentClientImpl(incidentClientDefinition);
    }

    @Bean
    IncidentPublisher incidentPublisher() {
        return new IncidentPublisherImpl(
                this.applicationEventPublisher
        );
    }

    @Bean
    IncidentSubscriber incidentSubscriber(IncidentClient incidentClient) {
        return new IncidentSubscriberImpl(
                incidentClient
        );
    }

    @Bean
    AsyncUncaughtExceptionHandler asyncUncaughtExceptionHandler() {
        return new AsyncUncaughtExceptionHandlerPublisher(
                this.incidentPublisher()
        );
    }

    @Bean
    ErrorHandler errorHandler() {
        return new ErrorHandlerPublisher(
                this.incidentPublisher()
        );
    }

    @Bean
    RejectedExecutionHandler rejectedExecutionHandler() {
        return new RejectedExecutionHandlerPublisher(
                this.incidentPublisher()
        );
    }
}
