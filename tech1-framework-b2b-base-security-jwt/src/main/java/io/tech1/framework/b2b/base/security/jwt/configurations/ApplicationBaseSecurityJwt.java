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
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
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

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        var basePathPrefix = this.applicationFrameworkProperties.getMvcConfigs().getFrameworkBasePathPrefix();

        http.cors(Customizer.withDefaults())
                .csrf(AbstractHttpConfigurer::disable)
                .addFilterBefore(
                        this.jwtTokensFilter,
                        UsernamePasswordAuthenticationFilter.class
                )
                .exceptionHandling(exceptionHandling ->
                        exceptionHandling
                                .authenticationEntryPoint(this.jwtAuthenticationEntryPointExceptionHandler)
                                .accessDeniedHandler(this.jwtAccessDeniedExceptionHandler)
                )
                .sessionManagement(sessionManagement ->
                        sessionManagement
                                .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                );

        http.authorizeHttpRequests(authorizeHttpRequests -> {
            authorizeHttpRequests
                    .requestMatchers(POST, basePathPrefix + "/authentication/login").permitAll()
                    .requestMatchers(POST, basePathPrefix + "/authentication/logout").permitAll()
                    .requestMatchers(POST, basePathPrefix + "/authentication/refreshToken").permitAll()
                    .requestMatchers(GET, basePathPrefix + "/session/current").authenticated()
                    .requestMatchers(POST, basePathPrefix + "/registration/register1").denyAll()
                    .requestMatchers(POST, basePathPrefix + "/user/update1").denyAll()
                    .requestMatchers(POST, basePathPrefix + "/user/update2").authenticated()
                    .requestMatchers(POST, basePathPrefix + "/user/changePassword1").denyAll();

            if (this.applicationFrameworkProperties.getSecurityJwtConfigs().getEssenceConfigs().getInvitationCodes().isEnabled()) {
                authorizeHttpRequests
                        .requestMatchers(GET, basePathPrefix + "/invitationCode").hasAuthority(INVITATION_CODE_READ)
                        .requestMatchers(POST, basePathPrefix + "/invitationCode").hasAuthority(INVITATION_CODE_WRITE)
                        .requestMatchers(DELETE, basePathPrefix + "/invitationCode/{invitationCodeId}").hasAuthority(INVITATION_CODE_WRITE);
            } else {
                authorizeHttpRequests.requestMatchers(basePathPrefix + "/invitationCode/**").denyAll();
            }

            authorizeHttpRequests
                    .requestMatchers(basePathPrefix + "/hardware/**").authenticated()
                    .requestMatchers(basePathPrefix + "/superadmin/**").hasAuthority(SUPERADMIN)
                    .requestMatchers(basePathPrefix + "/**").authenticated();

            authorizeHttpRequests.requestMatchers("/actuator/**").permitAll();

            authorizeHttpRequests.anyRequest().authenticated();
        });

        this.abstractApplicationSecurityJwtConfigurer.configure(http);

        return http.build();
    }
}
