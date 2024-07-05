package io.tech1.framework.iam.tests.contexts;

import io.tech1.framework.iam.assistants.current.CurrentSessionAssistant;
import io.tech1.framework.iam.assistants.userdetails.JwtUserDetailsService;
import io.tech1.framework.iam.events.publishers.SecurityJwtIncidentPublisher;
import io.tech1.framework.iam.events.publishers.SecurityJwtPublisher;
import io.tech1.framework.iam.handlers.exceptions.ResourceExceptionHandler;
import io.tech1.framework.iam.services.*;
import io.tech1.framework.iam.sessions.SessionRegistry;
import io.tech1.framework.iam.tokens.facade.TokensProvider;
import io.tech1.framework.iam.utils.SecurityJwtTokenUtils;
import io.tech1.framework.foundation.incidents.events.publishers.IncidentPublisher;
import io.tech1.framework.foundation.utilities.environment.EnvironmentUtility;
import io.tech1.framework.iam.validators.*;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

import static org.mockito.Mockito.mock;

@Configuration
@ComponentScan({
        "io.tech1.framework.iam.resources",
})
@EnableWebMvc
public class TestsApplicationResourcesContext {

    @Bean
    ResourceExceptionHandler resourceExceptionHandler() {
        return new ResourceExceptionHandler();
    }

    // =================================================================================================================
    // Authentication
    // =================================================================================================================
    @Bean
    AuthenticationManager authenticationManager() {
        return mock(AuthenticationManager.class);
    }

    // =================================================================================================================
    // Session
    // =================================================================================================================
    @Bean
    SessionRegistry sessionRegistry() {
        return mock(SessionRegistry.class);
    }

    // =================================================================================================================
    // Services
    // =================================================================================================================
    @Bean
    BaseUsersService userService() {
        return mock(BaseUsersService.class);
    }

    @Bean
    BaseInvitationCodesService invitationCodeService() {
        return mock(BaseInvitationCodesService.class);
    }

    @Bean
    BaseRegistrationService registrationService() {
        return mock(BaseRegistrationService.class);
    }

    @Bean
    BaseSuperadminService superadminService() {
        return mock(BaseSuperadminService.class);
    }

    @Bean
    TokensService tokenService() {
        return mock(TokensService.class);
    }

    @Bean
    BaseUsersSessionsService jwtRefreshTokenService() {
        return mock(BaseUsersSessionsService.class);
    }

    // =================================================================================================================
    // Assistants
    // =================================================================================================================
    @Bean
    CurrentSessionAssistant currentSessionAssistant() {
        return mock(CurrentSessionAssistant.class);
    }

    @Bean
    JwtUserDetailsService jwtUserDetailsAssistant() {
        return mock(JwtUserDetailsService.class);
    }

    // =================================================================================================================
    // Publishers
    // =================================================================================================================
    @Bean
    SecurityJwtPublisher securityJwtPublisher() {
        return mock(SecurityJwtPublisher.class);
    }

    @Bean
    SecurityJwtIncidentPublisher securityJwtIncidentPublisher() {
        return mock(SecurityJwtIncidentPublisher.class);
    }

    @Bean
    IncidentPublisher incidentPublisher() {
        return mock(IncidentPublisher.class);
    }

    // =================================================================================================================
    // Tokens
    // =================================================================================================================
    @Bean
    TokensProvider tokensProvider() {
        return mock(TokensProvider.class);
    }

    // =================================================================================================================
    // Utilities
    // =================================================================================================================
    @Bean
    SecurityJwtTokenUtils securityJwtTokenUtility() {
        return mock(SecurityJwtTokenUtils.class);
    }

    @Bean
    EnvironmentUtility environmentUtility() {
        return mock(EnvironmentUtility.class);
    }

    // =================================================================================================================
    // Validators
    // =================================================================================================================
    @Bean
    BaseAuthenticationRequestsValidator authenticationRequestsValidator() {
        return mock(BaseAuthenticationRequestsValidator.class);
    }

    @Bean
    BaseInvitationCodesRequestsValidator invitationCodeRequestsValidator() {
        return mock(BaseInvitationCodesRequestsValidator.class);
    }

    @Bean
    BaseRegistrationRequestsValidator registrationRequestsValidator() {
        return mock(BaseRegistrationRequestsValidator.class);
    }

    @Bean
    BaseUsersSessionsRequestsValidator sessionsRequestsValidator() {
        return mock(BaseUsersSessionsRequestsValidator.class);
    }

    @Bean
    BaseUsersValidator userRequestsValidator() {
        return mock(BaseUsersValidator.class);
    }
}
