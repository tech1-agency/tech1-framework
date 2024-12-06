package jbst.iam.resources.base;

import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jbst.foundation.domain.exceptions.tokens.UserEmailConfirmException;
import jbst.foundation.domain.exceptions.tokens.UserTokenValidationException;
import jbst.foundation.domain.properties.JbstProperties;
import jbst.foundation.incidents.events.publishers.IncidentPublisher;
import jbst.iam.annotations.AbstractJbstBaseSecurityResource;
import jbst.iam.services.BaseUsersTokensService;
import jbst.iam.validators.BaseUsersTokensRequestsValidator;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
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

    // Services
    private final BaseUsersTokensService baseUsersTokensService;
    // Validators
    private final BaseUsersTokensRequestsValidator baseUsersTokensRequestsValidator;
    // Incidents
    private final IncidentPublisher incidentPublisher;
    // Properties
    private final JbstProperties jbstProperties;

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
