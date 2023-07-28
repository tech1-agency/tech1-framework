package io.tech1.framework.b2b.base.security.jwt.configurations;

import io.tech1.framework.b2b.base.security.jwt.assistants.userdetails.JwtUserDetailsService;
import io.tech1.framework.b2b.base.security.jwt.filters.JwtTokensFilter;
import io.tech1.framework.b2b.base.security.jwt.handlers.exceptions.JwtAccessDeniedExceptionHandler;
import io.tech1.framework.b2b.base.security.jwt.handlers.exceptions.JwtAuthenticationEntryPointExceptionHandler;
import io.tech1.framework.configurations.jasypt.ApplicationJasypt;
import io.tech1.framework.configurations.server.ApplicationSpringBootServer;
import io.tech1.framework.emails.configurations.ApplicationEmails;
import io.tech1.framework.incidents.configurations.ApplicationIncidents;
import io.tech1.framework.properties.ApplicationFrameworkProperties;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import javax.annotation.PostConstruct;

import static io.tech1.framework.domain.base.AbstractAuthority.*;
import static io.tech1.framework.domain.properties.utilities.PropertiesAsserter.assertProperties;
import static org.springframework.http.HttpMethod.*;

@Configuration
@ComponentScan({
        // -------------------------------------------------------------------------------------------------------------
        "io.tech1.framework.b2b.base.security.jwt.cookies",
        "io.tech1.framework.b2b.base.security.jwt.crons",
        "io.tech1.framework.b2b.base.security.jwt.events",
        "io.tech1.framework.b2b.base.security.jwt.filters",
        "io.tech1.framework.b2b.base.security.jwt.handlers.exceptions",
        "io.tech1.framework.b2b.base.security.jwt.incidents.converters",
        "io.tech1.framework.b2b.base.security.jwt.resources",
        "io.tech1.framework.b2b.base.security.jwt.services",
        "io.tech1.framework.b2b.base.security.jwt.utils",
        "io.tech1.framework.b2b.base.security.jwt.validators",
        // -------------------------------------------------------------------------------------------------------------
        "io.tech1.framework.utilities.browsers",
        "io.tech1.framework.utilities.geo"
        // -------------------------------------------------------------------------------------------------------------
})
@EnableWebSecurity
@Import({
        ApplicationSpringBootServer.class,
        ApplicationJasypt.class,
        ApplicationBaseSecurityJwtMvc.class,
        ApplicationIncidents.class,
        ApplicationEmails.class
})
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class ApplicationBaseSecurityJwt extends WebSecurityConfigurerAdapter {

    // Assistants
    private final JwtUserDetailsService jwtUserDetailsService;
    // Filters
    private final JwtTokensFilter jwtTokensFilter;
    // Handlers
    private final JwtAuthenticationEntryPointExceptionHandler jwtAuthenticationEntryPointExceptionHandler;
    private final JwtAccessDeniedExceptionHandler jwtAccessDeniedExceptionHandler;
    // Configurer
    private final AbstractApplicationSecurityJwtConfigurer abstractApplicationSecurityJwtConfigurer;
    // Properties
    private final ApplicationFrameworkProperties applicationFrameworkProperties;

    @PostConstruct
    public void init() {
        var securityJwtConfigs = this.applicationFrameworkProperties.getSecurityJwtConfigs();
        assertProperties(securityJwtConfigs, "securityJwtConfigs");
    }

    @Override
    protected void configure(AuthenticationManagerBuilder authenticationManagerBuilder) throws Exception {
        authenticationManagerBuilder
                .userDetailsService(this.jwtUserDetailsService)
                .passwordEncoder(this.bCryptPasswordEncoder());
    }

    @Bean
    @Override
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }

    @Override
    public void configure(WebSecurity web) throws Exception {
        this.abstractApplicationSecurityJwtConfigurer.configure(web);
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        var basePathPrefix = this.applicationFrameworkProperties.getMvcConfigs().getFrameworkBasePathPrefix();
        http.cors()
                .and()
                    .csrf()
                        .disable()
                    .addFilterBefore(
                            this.jwtTokensFilter,
                            UsernamePasswordAuthenticationFilter.class
                    )
                    .exceptionHandling()
                        .authenticationEntryPoint(this.jwtAuthenticationEntryPointExceptionHandler)
                        .accessDeniedHandler(this.jwtAccessDeniedExceptionHandler)
                .and()
                    .sessionManagement()
                    .sessionCreationPolicy(SessionCreationPolicy.STATELESS);

        var urlRegistry = http.authorizeRequests();
        urlRegistry.antMatchers(POST, basePathPrefix + "/authentication/login").anonymous();
        urlRegistry.antMatchers(POST, basePathPrefix +"/authentication/logout").permitAll();
        urlRegistry.antMatchers(POST, basePathPrefix + "/authentication/refreshToken").permitAll();
        urlRegistry.antMatchers(GET, basePathPrefix + "/session/current").authenticated();
        urlRegistry.antMatchers(POST, basePathPrefix + "/registration/register1").denyAll();
        urlRegistry.antMatchers(POST, basePathPrefix + "/user/update1").denyAll();
        urlRegistry.antMatchers(POST, basePathPrefix + "/user/update2").authenticated();
        urlRegistry.antMatchers(POST, basePathPrefix + "/user/changePassword1").denyAll();

        if (this.applicationFrameworkProperties.getSecurityJwtConfigs().getEssenceConfigs().getInvitationCodes().isEnabled()) {
            urlRegistry.antMatchers(GET, basePathPrefix + "/invitationCode").hasAuthority(INVITATION_CODE_READ);
            urlRegistry.antMatchers(POST, basePathPrefix + "/invitationCode").hasAuthority(INVITATION_CODE_WRITE);
            urlRegistry.antMatchers(DELETE, basePathPrefix + "/invitationCode/{invitationCodeId}").hasAuthority(INVITATION_CODE_WRITE);
        } else {
            urlRegistry.antMatchers( basePathPrefix + "/invitationCode/**").denyAll();
        }
        urlRegistry.antMatchers(basePathPrefix + "/hardware/**").authenticated();
        urlRegistry.antMatchers(basePathPrefix + "/superadmin/**").hasAuthority(SUPER_ADMIN);
        urlRegistry.antMatchers(basePathPrefix + "/**").authenticated();

        urlRegistry.antMatchers("/actuator/**").permitAll();

        this.abstractApplicationSecurityJwtConfigurer.configure(http);

        urlRegistry.anyRequest().authenticated();
    }

    @Bean
    public BCryptPasswordEncoder bCryptPasswordEncoder() {
        return new BCryptPasswordEncoder(11);
    }
}
