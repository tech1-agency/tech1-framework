package io.tech1.framework.incidents.configurations;

import feign.Feign;
import feign.auth.BasicAuthRequestInterceptor;
import feign.jackson.JacksonDecoder;
import feign.jackson.JacksonEncoder;
import feign.okhttp.OkHttpClient;
import io.tech1.framework.domain.base.PropertyId;
import io.tech1.framework.incidents.feigns.definitions.IncidentClientDefinition;
import io.tech1.framework.incidents.feigns.definitions.IncidentClientSlf4j;
import io.tech1.framework.properties.ApplicationFrameworkProperties;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;

import javax.annotation.PostConstruct;

@Slf4j
@ComponentScan({
        // -------------------------------------------------------------------------------------------------------------
        "io.tech1.framework.incidents"
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
    public IncidentClientDefinition incidentClientDefinition() {
        var incidentConfigs = this.applicationFrameworkProperties.getIncidentConfigs();
        if (incidentConfigs.isEnabled()) {
            var incidentServer = incidentConfigs.getRemoteServer();
            var incidentServerBaseURL = incidentServer.getBaseURL();
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
                    .target(IncidentClientDefinition.class, incidentServerBaseURL);
        } else {
            return new IncidentClientSlf4j();
        }
    }
}
