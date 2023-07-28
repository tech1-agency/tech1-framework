package io.tech1.framework.b2b.mongodb.security.jwt.resources;

import io.tech1.framework.b2b.base.security.jwt.annotations.AbstractFrameworkBaseSecurityResource;
import io.tech1.framework.b2b.base.security.jwt.domain.dto.requests.RequestUserChangePassword1;
import io.tech1.framework.b2b.base.security.jwt.domain.dto.requests.RequestUserUpdate1;
import io.tech1.framework.b2b.base.security.jwt.domain.dto.requests.RequestUserUpdate2;
import io.tech1.framework.b2b.mongodb.security.jwt.assistants.core.CurrentSessionAssistant;
import io.tech1.framework.b2b.mongodb.security.jwt.services.BaseUserService;
import io.tech1.framework.b2b.base.security.jwt.validators.BaseUserValidator;
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
        var username = this.currentSessionAssistant.getCurrentUsername();
        this.baseUserValidator.validateUserUpdateRequest1(username, requestUserUpdate1);
        this.baseUserService.updateUser1(requestUserUpdate1);
    }

    @PostMapping("/update2")
    @ResponseStatus(HttpStatus.OK)
    public void update1(@RequestBody RequestUserUpdate2 requestUserUpdate2) {
        this.baseUserValidator.validateUserUpdateRequest2(requestUserUpdate2);
        this.baseUserService.updateUser2(requestUserUpdate2);
    }

    @PostMapping("/changePassword1")
    @ResponseStatus(HttpStatus.OK)
    public void changePassword(@RequestBody RequestUserChangePassword1 requestUserChangePassword1) {
        this.baseUserValidator.validateUserChangePasswordRequest1(requestUserChangePassword1);
        this.baseUserService.changePassword1(requestUserChangePassword1);
    }
}
