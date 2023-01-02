package io.tech1.framework.b2b.mongodb.security.jwt.tests.contexts;

import io.tech1.framework.b2b.mongodb.security.jwt.assistants.core.CurrentSessionAssistant;
import io.tech1.framework.b2b.mongodb.security.jwt.assistants.userdetails.JwtUserDetailsAssistant;
import io.tech1.framework.b2b.mongodb.security.jwt.cookies.CookieProvider;
import io.tech1.framework.b2b.mongodb.security.jwt.events.publishers.SecurityJwtIncidentPublisher;
import io.tech1.framework.b2b.mongodb.security.jwt.events.publishers.SecurityJwtPublisher;
import io.tech1.framework.b2b.mongodb.security.jwt.handlers.exceptions.ResourceExceptionHandler;
import io.tech1.framework.b2b.mongodb.security.jwt.services.*;
import io.tech1.framework.b2b.mongodb.security.jwt.sessions.SessionRegistry;
import io.tech1.framework.b2b.mongodb.security.jwt.utilities.SecurityJwtTokenUtility;
import io.tech1.framework.b2b.mongodb.security.jwt.validators.AuthenticationRequestsValidator;
import io.tech1.framework.b2b.mongodb.security.jwt.validators.BaseUserValidator;
import io.tech1.framework.b2b.mongodb.security.jwt.validators.InvitationCodeRequestsValidator;
import io.tech1.framework.b2b.mongodb.security.jwt.validators.RegistrationRequestsValidator;
import io.tech1.framework.incidents.events.publishers.IncidentPublisher;
import io.tech1.framework.utilities.environment.EnvironmentUtility;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

import static org.mockito.Mockito.mock;

@Configuration
@ComponentScan({
        "io.tech1.framework.b2b.mongodb.security.jwt.resources"
})
@EnableWebMvc
public class TestsApplicationResourcesContext {

    @Bean
    public ResourceExceptionHandler resourceExceptionHandler() {
        return new ResourceExceptionHandler();
    }

    // =================================================================================================================
    // Authentication
    // =================================================================================================================
    @Bean
    public AuthenticationManager authenticationManager() {
        return mock(AuthenticationManager.class);
    }

    // =================================================================================================================
    // Session
    // =================================================================================================================
    @Bean
    public SessionRegistry sessionRegistry() {
        return mock(SessionRegistry.class);
    }

    // =================================================================================================================
    // Services
    // =================================================================================================================
    @Bean
    public BaseUserService userService() {
        return mock(BaseUserService.class);
    }

    @Bean
    public InvitationCodeService invitationCodeService() {
        return mock(InvitationCodeService.class);
    }

    @Bean
    public RegistrationService registrationService() {
        return mock(RegistrationService.class);
    }

    @Bean
    public SuperAdminService superAdminService() {
        return mock(SuperAdminService.class);
    }

    @Bean
    public TokenService tokenService() {
        return mock(TokenService.class);
    }

    @Bean
    public UserSessionService jwtRefreshTokenService() {
        return mock(UserSessionService.class);
    }

    // =================================================================================================================
    // Assistants
    // =================================================================================================================
    @Bean
    public CurrentSessionAssistant currentSessionAssistant() {
        return mock(CurrentSessionAssistant.class);
    }

    @Bean
    public JwtUserDetailsAssistant jwtUserDetailsAssistant() {
        return mock(JwtUserDetailsAssistant.class);
    }

    // =================================================================================================================
    // Publishers
    // =================================================================================================================
    @Bean
    public SecurityJwtPublisher securityJwtPublisher() {
        return mock(SecurityJwtPublisher.class);
    }

    @Bean
    public SecurityJwtIncidentPublisher securityJwtIncidentPublisher() {
        return mock(SecurityJwtIncidentPublisher.class);
    }

    @Bean
    public IncidentPublisher incidentPublisher() {
        return mock(IncidentPublisher.class);
    }

    // =================================================================================================================
    // Cookies
    // =================================================================================================================
    @Bean
    public CookieProvider cookieProvider() {
        return mock(CookieProvider.class);
    }

    // =================================================================================================================
    // Utilities
    // =================================================================================================================
    @Bean
    public SecurityJwtTokenUtility securityJwtTokenUtility() {
        return mock(SecurityJwtTokenUtility.class);
    }

    @Bean
    public EnvironmentUtility environmentUtility() {
        return mock(EnvironmentUtility.class);
    }

    // =================================================================================================================
    // Validators
    // =================================================================================================================
    @Bean
    public AuthenticationRequestsValidator authenticationRequestsValidator() {
        return mock(AuthenticationRequestsValidator.class);
    }

    @Bean
    public InvitationCodeRequestsValidator invitationCodeRequestsValidator() {
        return mock(InvitationCodeRequestsValidator.class);
    }

    @Bean
    public RegistrationRequestsValidator registrationRequestsValidator() {
        return mock(RegistrationRequestsValidator.class);
    }

    @Bean
    public BaseUserValidator userRequestsValidator() {
        return mock(BaseUserValidator.class);
    }
}
