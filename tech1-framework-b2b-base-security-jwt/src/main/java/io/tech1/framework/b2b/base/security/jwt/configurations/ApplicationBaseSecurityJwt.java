package io.tech1.framework.b2b.base.security.jwt.configurations;

import io.tech1.framework.b2b.base.security.jwt.assistants.userdetails.JwtUserDetailsService;
import io.tech1.framework.b2b.base.security.jwt.filters.jwt.JwtTokensFilter;
import io.tech1.framework.b2b.base.security.jwt.handlers.exceptions.JwtAccessDeniedExceptionHandler;
import io.tech1.framework.b2b.base.security.jwt.handlers.exceptions.JwtAuthenticationEntryPointExceptionHandler;
import io.tech1.framework.configurations.jasypt.ApplicationJasypt;
import io.tech1.framework.configurations.server.ApplicationSpringBootServer;
import io.tech1.framework.domain.base.PropertyId;
import io.tech1.framework.emails.configurations.ApplicationEmails;
import io.tech1.framework.incidents.configurations.ApplicationIncidents;
import io.tech1.framework.properties.ApplicationFrameworkProperties;
import io.tech1.framework.utilities.configurations.ApplicationUtilities;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import jakarta.annotation.PostConstruct;

import static io.tech1.framework.domain.base.AbstractAuthority.*;
import static org.springframework.http.HttpMethod.*;

@Configuration
@ComponentScan({
        // -------------------------------------------------------------------------------------------------------------
        "io.tech1.framework.b2b.base.security.jwt.crons",
        "io.tech1.framework.b2b.base.security.jwt.events",
        "io.tech1.framework.b2b.base.security.jwt.handlers.exceptions",
        "io.tech1.framework.b2b.base.security.jwt.resources",
        "io.tech1.framework.b2b.base.security.jwt.services",
        "io.tech1.framework.b2b.base.security.jwt.tokens",
        "io.tech1.framework.b2b.base.security.jwt.utils",
        "io.tech1.framework.b2b.base.security.jwt.validators"
        // -------------------------------------------------------------------------------------------------------------
})
@EnableWebSecurity
@Import({
        ApplicationUtilities.class,
        ApplicationSpringBootServer.class,
        ApplicationJasypt.class,
        ApplicationBaseSecurityJwtMvc.class,
        ApplicationBaseSecurityJwtFilters.class,
        ApplicationBaseSecurityJwtPasswords.class,
        ApplicationIncidents.class,
        ApplicationEmails.class
})
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class ApplicationBaseSecurityJwt {

    // Assistants
    private final JwtUserDetailsService jwtUserDetailsService;
    // Passwords
    private final BCryptPasswordEncoder bCryptPasswordEncoder;
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
        this.applicationFrameworkProperties.getSecurityJwtConfigs().assertProperties(new PropertyId("securityJwtConfigs"));
    }

    @Autowired
    void configureAuthenticationManager(AuthenticationManagerBuilder builder) throws Exception {
        builder
                .userDetailsService(this.jwtUserDetailsService)
                .passwordEncoder(this.bCryptPasswordEncoder);
    }

    @Bean
    AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }

    @Bean
    public WebSecurityCustomizer webSecurityCustomizer() {
        return this.abstractApplicationSecurityJwtConfigurer::configure;
    }

    // TODO [VB] deprecated
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        var basePathPrefix = this.applicationFrameworkProperties.getMvcConfigs().getFrameworkBasePathPrefix();
        http.cors()
                .and()
                    .csrf().disable()
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
        urlRegistry.antMatchers(POST, basePathPrefix + "/authentication/login").permitAll();
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
        urlRegistry.antMatchers(basePathPrefix + "/superadmin/**").hasAuthority(SUPERADMIN);
        urlRegistry.antMatchers(basePathPrefix + "/**").authenticated();

        urlRegistry.antMatchers("/actuator/**").permitAll();

        this.abstractApplicationSecurityJwtConfigurer.configure(http);

        urlRegistry.anyRequest().authenticated();
        return http.build();
    }
}
