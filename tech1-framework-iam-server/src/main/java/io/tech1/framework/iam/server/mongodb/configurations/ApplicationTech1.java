package io.tech1.framework.iam.server.mongodb.configurations;

import io.tech1.framework.foundation.domain.base.PropertyId;
import io.tech1.framework.iam.configurations.AbstractApplicationSecurityJwtConfigurer;
import io.tech1.framework.iam.configurations.ApplicationBaseSecurityJwt;
import io.tech1.framework.iam.configurations.ApplicationMongo;
import io.tech1.framework.iam.server.base.properties.ApplicationProperties;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;

@Configuration
@ComponentScan({
        // -------------------------------------------------------------------------------------------------------------
        "io.tech1.framework.iam.filters.jwt_extension",
        // -------------------------------------------------------------------------------------------------------------
})
@Import({
        ApplicationBaseSecurityJwt.class,
        ApplicationMongo.class
})
@EnableConfigurationProperties({
        ApplicationProperties.class
})
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class ApplicationTech1 implements AbstractApplicationSecurityJwtConfigurer {

    // Properties
    private final ApplicationProperties applicationProperties;

    @PostConstruct
    public void init() {
        this.applicationProperties.getServerConfigs().assertProperties(new PropertyId("serverConfigs"));
    }

    @Override
    public void configure(WebSecurity webSecurity) {
        // no tech1-server configurations yet
    }

    @Override
    public void configure(HttpSecurity http) throws Exception {
        http.authorizeHttpRequests(authorizeHttpRequests ->
                authorizeHttpRequests
                        .requestMatchers("/hardware/**").permitAll()
        );
    }

}
