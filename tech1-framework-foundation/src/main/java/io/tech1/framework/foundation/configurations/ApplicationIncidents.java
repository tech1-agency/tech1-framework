package io.tech1.framework.foundation.configurations;

import feign.Feign;
import feign.auth.BasicAuthRequestInterceptor;
import feign.jackson.JacksonDecoder;
import feign.jackson.JacksonEncoder;
import feign.okhttp.OkHttpClient;
import io.tech1.framework.foundation.domain.base.PropertyId;
import io.tech1.framework.foundation.domain.properties.ApplicationFrameworkProperties;
import io.tech1.framework.foundation.incidents.events.publishers.IncidentPublisher;
import io.tech1.framework.foundation.incidents.events.publishers.impl.IncidentPublisherImpl;
import io.tech1.framework.foundation.incidents.events.subscribers.IncidentSubscriber;
import io.tech1.framework.foundation.incidents.events.subscribers.impl.IncidentSubscriberImpl;
import io.tech1.framework.foundation.incidents.feigns.clients.IncidentClient;
import io.tech1.framework.foundation.incidents.feigns.clients.impl.IncidentClientImpl;
import io.tech1.framework.foundation.incidents.feigns.definitions.IncidentClientDefinition;
import io.tech1.framework.foundation.incidents.feigns.definitions.IncidentClientDefinitionSlf4j;
import io.tech1.framework.foundation.incidents.handlers.AsyncUncaughtExceptionHandlerPublisher;
import io.tech1.framework.foundation.incidents.handlers.ErrorHandlerPublisher;
import io.tech1.framework.foundation.incidents.handlers.RejectedExecutionHandlerPublisher;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.aop.interceptor.AsyncUncaughtExceptionHandler;
import org.springframework.beans.factory.annotation.Autowired;
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
    IncidentClientDefinition incidentClientDefinition() {
        var incidentConfigs = this.applicationFrameworkProperties.getIncidentConfigs();
        if (incidentConfigs.isEnabled()) {
            var incidentServer = incidentConfigs.getRemoteServer();
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
        } else {
            return new IncidentClientDefinitionSlf4j();
        }
    }

    @Bean
    IncidentClient incidentClient() {
        return new IncidentClientImpl(this.incidentClientDefinition());
    }

    @Bean
    IncidentPublisher incidentPublisher() {
        return new IncidentPublisherImpl(
                this.applicationEventPublisher
        );
    }

    @Bean
    IncidentSubscriber incidentSubscriber() {
        return new IncidentSubscriberImpl(
                this.incidentClient()
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
