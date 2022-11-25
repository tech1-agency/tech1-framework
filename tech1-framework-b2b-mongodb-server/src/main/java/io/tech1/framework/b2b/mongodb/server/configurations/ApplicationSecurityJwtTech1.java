package io.tech1.framework.b2b.mongodb.server.configurations;

import io.tech1.framework.b2b.mongodb.security.jwt.configurations.AbstractApplicationSecurityJwtConfigurer;
import io.tech1.framework.b2b.mongodb.security.jwt.configurations.ApplicationBaseSecurityJwt;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;

@Configuration
@ComponentScan({
        // -------------------------------------------------------------------------------------------------------------
        "io.tech1.framework.b2b.mongodb.security.jwt.assistants.core"
        // -------------------------------------------------------------------------------------------------------------
})
@Import({
        ApplicationBaseSecurityJwt.class
})
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class ApplicationSecurityJwtTech1 implements AbstractApplicationSecurityJwtConfigurer {

    @Override
    public void configure(WebSecurity webSecurity) {
        // no tech1-server configurations yet
    }

    @Override
    public void configure(HttpSecurity http) throws Exception {
        var urlRegistry = http.authorizeRequests();
        urlRegistry.antMatchers("/hardware/**").permitAll();
    }
}
