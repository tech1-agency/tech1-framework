package jbst.iam.server.configurations;

import jakarta.annotation.PostConstruct;
import jbst.iam.configurations.AbstractApplicationSecurityJwtConfigurer;
import jbst.iam.configurations.ApplicationBaseSecurityJwtWebsockets;
import jbst.iam.server.base.properties.ApplicationProperties;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import tech1.framework.foundation.domain.base.PropertyId;
import tech1.framework.foundation.domain.properties.ApplicationFrameworkProperties;

import static org.springframework.http.HttpMethod.GET;

@Configuration
@ComponentScan({
        // -------------------------------------------------------------------------------------------------------------
        "jbst.iam.assistants.current.base",
        "jbst.iam.filters.jwt_extension",
        "jbst.iam.tasks.superadmin"
        // -------------------------------------------------------------------------------------------------------------
})
@Import({
        ApplicationBaseSecurityJwtWebsockets.class
})
@EnableConfigurationProperties({
        ApplicationProperties.class
})
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class ConfigurationServer implements AbstractApplicationSecurityJwtConfigurer {

    // Properties
    private final ApplicationFrameworkProperties applicationFrameworkProperties;
    private final ApplicationProperties applicationProperties;

    @PostConstruct
    public void init() {
        this.applicationProperties.getServerConfigs().assertProperties(new PropertyId("serverConfigs"));
    }

    @Override
    public void configure(WebSecurity web) {
        var endpoint = this.applicationFrameworkProperties.getSecurityJwtWebsocketsConfigs().getStompConfigs().getEndpoint();
        web.ignoring().requestMatchers(endpoint + "/**");
    }

    @Override
    public void configure(HttpSecurity http) throws Exception {
        http
                .csrf(csrf -> csrf.ignoringRequestMatchers("/**"))
                .authorizeHttpRequests(authorizeHttpRequests ->
                        authorizeHttpRequests
                                .requestMatchers(GET, "/system/csrf").authenticated()
                                .requestMatchers("/hardware/**").permitAll()
                );
    }

}
