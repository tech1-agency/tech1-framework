package io.tech1.framework.incidents.configurations;

import feign.Feign;
import feign.auth.BasicAuthRequestInterceptor;
import feign.jackson.JacksonDecoder;
import feign.jackson.JacksonEncoder;
import feign.okhttp.OkHttpClient;
import io.tech1.framework.domain.base.PropertyId;
import io.tech1.framework.domain.properties.ApplicationFrameworkProperties;
import io.tech1.framework.incidents.feigns.clients.IncidentClient;
import io.tech1.framework.incidents.feigns.clients.impl.IncidentClientImpl;
import io.tech1.framework.incidents.feigns.definitions.IncidentClientDefinition;
import io.tech1.framework.incidents.feigns.definitions.IncidentClientDefinitionSlf4j;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;

@Slf4j
@ComponentScan({
        // -------------------------------------------------------------------------------------------------------------
        "io.tech1.framework.incidents.configurations",
        "io.tech1.framework.incidents.events",
        "io.tech1.framework.incidents.handlers"
//        "io.tech1.framework.incidents"
        // -------------------------------------------------------------------------------------------------------------
})
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class ApplicationIncidents {

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
}
