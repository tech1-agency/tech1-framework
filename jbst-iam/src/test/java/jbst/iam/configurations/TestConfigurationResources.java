package jbst.iam.configurations;

import jbst.iam.assistants.current.CurrentSessionAssistant;
import jbst.iam.assistants.userdetails.JwtUserDetailsService;
import jbst.iam.events.publishers.SecurityJwtIncidentPublisher;
import jbst.iam.events.publishers.SecurityJwtPublisher;
import jbst.iam.handlers.exceptions.ResourceExceptionHandler;
import jbst.iam.services.*;
import jbst.iam.sessions.SessionRegistry;
import jbst.iam.tokens.facade.TokensProvider;
import jbst.iam.utils.SecurityJwtTokenUtils;
import jbst.iam.validators.*;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import jbst.foundation.incidents.events.publishers.IncidentPublisher;
import jbst.foundation.utilities.environment.EnvironmentUtility;

import static org.mockito.Mockito.mock;

@Configuration
@ComponentScan({
        "jbst.iam.resources",
})
@EnableWebMvc
public class TestConfigurationResources {

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
    BaseInvitationsService invitationCodeService() {
        return mock(BaseInvitationsService.class);
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
    BaseInvitationsRequestsValidator invitationCodeRequestsValidator() {
        return mock(BaseInvitationsRequestsValidator.class);
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
