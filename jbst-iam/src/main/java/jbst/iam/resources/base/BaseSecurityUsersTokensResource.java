package jbst.iam.resources.base;

import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jbst.foundation.domain.base.Username;
import jbst.foundation.domain.concurrent.RateLimiter;
import jbst.foundation.domain.exceptions.base.TooManyRequestsException;
import jbst.foundation.domain.exceptions.tokens.UserEmailConfirmException;
import jbst.foundation.domain.exceptions.tokens.UserTokenValidationException;
import jbst.foundation.domain.factories.concurrent.RateLimiterFactory;
import jbst.foundation.domain.properties.JbstProperties;
import jbst.foundation.incidents.events.publishers.IncidentPublisher;
import jbst.iam.annotations.AbstractJbstBaseSecurityResource;
import jbst.iam.assistants.current.CurrentSessionAssistant;
import jbst.iam.domain.dto.requests.RequestUserToken;
import jbst.iam.services.BaseUsersTokensService;
import jbst.iam.services.base.BaseUsersEmailsService;
import jbst.iam.validators.BaseUsersTokensRequestsValidator;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.web.servlet.view.RedirectView;

// Swagger
@Tag(name = "[jbst] Tokens API")
// Spring
@Slf4j
@AbstractJbstBaseSecurityResource
@RestController
@RequestMapping("/tokens")
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class BaseSecurityUsersTokensResource {

    // Assistants
    private final CurrentSessionAssistant currentSessionAssistant;
    // Services
    private final BaseUsersTokensService baseUsersTokensService;
    private final BaseUsersEmailsService baseUsersEmailsService;
    // Validators
    private final BaseUsersTokensRequestsValidator baseUsersTokensRequestsValidator;
    // Incidents
    private final IncidentPublisher incidentPublisher;
    // Properties
    private final JbstProperties jbstProperties;

    private final RateLimiter<Username> executeConfirmEmailLimiter = RateLimiterFactory.executeConfirmEmail();

    @PostMapping("/email/confirm")
    @ResponseStatus(HttpStatus.OK)
    public void executeConfirmEmail() throws TooManyRequestsException {
        var user = this.currentSessionAssistant.getCurrentJwtUser();
        this.executeConfirmEmailLimiter.acquire(user.username());
        this.baseUsersTokensRequestsValidator.validateExecuteConfirmEmail(user);
        var requestUserToken = RequestUserToken.emailConfirmation(user.username());
        var userToken = this.baseUsersTokensService.getOrCreate(requestUserToken);
        this.baseUsersEmailsService.executeConfirmEmail(userToken.asFunctionConfirmEmail(user.email()));
    }

    @ApiResponse(responseCode = "302", content = @Content(schema = @Schema(implementation = String.class)))
    @GetMapping("/email/confirm")
    public RedirectView confirmEmail(
            RedirectAttributes redirectAttributes,
            @RequestParam("token") String token
    ) {
        var webclientURL = this.jbstProperties.getServerConfigs().getWebclientURL();
        var usersTokensConfigs = this.jbstProperties.getSecurityJwtConfigs().getUsersTokensConfigs();
        var redirectView = new RedirectView(usersTokensConfigs.getEmailConfirmRedirectURL(webclientURL));
        try {
            this.baseUsersTokensRequestsValidator.validateEmailConfirmationToken(token);
            this.baseUsersTokensService.confirmEmail(token);
            redirectAttributes.addAttribute("code", 1);
            return redirectView;
        } catch (UserTokenValidationException | UserEmailConfirmException ex) {
            redirectAttributes.addAttribute("code", 0);
            return redirectView;
        } catch (RuntimeException ex) {
            this.incidentPublisher.publishThrowable(ex);
            redirectAttributes.addAttribute("code", 0);
            return redirectView;
        }
    }

}
