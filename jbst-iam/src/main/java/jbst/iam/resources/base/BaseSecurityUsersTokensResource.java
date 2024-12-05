package jbst.iam.resources.base;

import io.swagger.v3.oas.annotations.tags.Tag;
import jbst.foundation.domain.exceptions.tokens.UserEmailConfirmException;
import jbst.foundation.domain.exceptions.tokens.UserTokenValidationException;
import jbst.iam.annotations.AbstractJbstBaseSecurityResource;
import jbst.iam.services.BaseUsersTokensService;
import jbst.iam.validators.BaseUsersTokensRequestsValidator;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

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

    @GetMapping("/email/confirm")
    @ResponseStatus(HttpStatus.OK)
    public void confirmEmail(@RequestParam("token") String token) throws UserTokenValidationException, UserEmailConfirmException {
        this.baseUsersTokensRequestsValidator.validateEmailConfirmationToken(token);
        this.baseUsersTokensService.confirmEmail(token);
    }

}
