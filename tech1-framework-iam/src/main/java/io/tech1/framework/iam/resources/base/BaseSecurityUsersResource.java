package io.tech1.framework.iam.resources.base;

import io.swagger.v3.oas.annotations.tags.Tag;
import io.tech1.framework.iam.annotations.AbstractFrameworkBaseSecurityResource;
import io.tech1.framework.iam.assistants.current.CurrentSessionAssistant;
import io.tech1.framework.iam.domain.dto.requests.RequestUserChangePasswordBasic;
import io.tech1.framework.iam.domain.dto.requests.RequestUserUpdate1;
import io.tech1.framework.iam.domain.dto.requests.RequestUserUpdate2;
import io.tech1.framework.iam.services.BaseUsersService;
import io.tech1.framework.iam.validators.BaseUsersValidator;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;

// Swagger
@Tag(name = "[tech1-framework] Users API")
// Spring
@SuppressWarnings("SpringJavaInjectionPointsAutowiringInspection")
@Slf4j
@AbstractFrameworkBaseSecurityResource
@RestController
@RequestMapping("/users")
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class BaseSecurityUsersResource {

    // Assistants
    private final CurrentSessionAssistant currentSessionAssistant;
    // Services
    private final BaseUsersService baseUsersService;
    // Validators
    private final BaseUsersValidator baseUsersValidator;

    @PostMapping("/update1")
    @ResponseStatus(HttpStatus.OK)
    public void update1(@RequestBody @Valid RequestUserUpdate1 request) {
        var user = this.currentSessionAssistant.getCurrentJwtUser();
        this.baseUsersValidator.validateUserUpdateRequest1(user.username(), request);
        this.baseUsersService.updateUser1(user, request);
    }

    @PostMapping("/update2")
    @ResponseStatus(HttpStatus.OK)
    public void update1(@RequestBody @Valid RequestUserUpdate2 request) {
        var user = this.currentSessionAssistant.getCurrentJwtUser();
        this.baseUsersService.updateUser2(user, request);
    }

    @PostMapping("/changePasswordRequired")
    @ResponseStatus(HttpStatus.OK)
    public void changePasswordRequired(@RequestBody @Valid RequestUserChangePasswordBasic request) {
        this.baseUsersValidator.validateUserChangePasswordRequestBasic(request);
        var user = this.currentSessionAssistant.getCurrentJwtUser();
        this.baseUsersService.changePasswordRequired(user, request);
    }

    @PostMapping("/changePassword1")
    @ResponseStatus(HttpStatus.OK)
    public void changePassword(@RequestBody @Valid RequestUserChangePasswordBasic request) {
        this.baseUsersValidator.validateUserChangePasswordRequestBasic(request);
        var user = this.currentSessionAssistant.getCurrentJwtUser();
        this.baseUsersService.changePassword1(user, request);
    }
}
