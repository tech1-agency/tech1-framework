package io.tech1.framework.b2b.mongodb.security.jwt.resources;

import io.tech1.framework.b2b.base.security.jwt.annotations.AbstractFrameworkBaseSecurityResource;
import io.tech1.framework.b2b.base.security.jwt.domain.dto.requests.RequestUserChangePassword1;
import io.tech1.framework.b2b.base.security.jwt.domain.dto.requests.RequestUserUpdate1;
import io.tech1.framework.b2b.base.security.jwt.domain.dto.requests.RequestUserUpdate2;
import io.tech1.framework.b2b.base.security.jwt.validators.BaseUserValidator;
import io.tech1.framework.b2b.base.security.jwt.assistants.current.CurrentSessionAssistant;
import io.tech1.framework.b2b.mongodb.security.jwt.services.BaseUserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@Slf4j
@AbstractFrameworkBaseSecurityResource
@RestController
@RequestMapping("/user")
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class BaseSecurityUserResource {

    // Services
    private final BaseUserService baseUserService;
    // Assistants
    private final CurrentSessionAssistant currentSessionAssistant;
    // Validators
    private final BaseUserValidator baseUserValidator;

    @PostMapping("/update1")
    @ResponseStatus(HttpStatus.OK)
    public void update1(@RequestBody RequestUserUpdate1 requestUserUpdate1) {
        var user = this.currentSessionAssistant.getCurrentJwtUser();
        this.baseUserValidator.validateUserUpdateRequest1(user.username(), requestUserUpdate1);
        this.baseUserService.updateUser1(user, requestUserUpdate1);
    }

    @PostMapping("/update2")
    @ResponseStatus(HttpStatus.OK)
    public void update1(@RequestBody RequestUserUpdate2 requestUserUpdate2) {
        var user = this.currentSessionAssistant.getCurrentJwtUser();
        this.baseUserValidator.validateUserUpdateRequest2(requestUserUpdate2);
        this.baseUserService.updateUser2(user, requestUserUpdate2);
    }

    @PostMapping("/changePassword1")
    @ResponseStatus(HttpStatus.OK)
    public void changePassword(@RequestBody RequestUserChangePassword1 requestUserChangePassword1) {
        var user = this.currentSessionAssistant.getCurrentJwtUser();
        this.baseUserValidator.validateUserChangePasswordRequest1(requestUserChangePassword1);
        this.baseUserService.changePassword1(user, requestUserChangePassword1);
    }
}
