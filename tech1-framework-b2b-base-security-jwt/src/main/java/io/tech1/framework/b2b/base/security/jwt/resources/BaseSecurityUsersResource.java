package io.tech1.framework.b2b.base.security.jwt.resources;

import io.tech1.framework.b2b.base.security.jwt.annotations.AbstractFrameworkBaseSecurityResource;
import io.tech1.framework.b2b.base.security.jwt.assistants.current.CurrentSessionAssistant;
import io.tech1.framework.b2b.base.security.jwt.domain.dto.requests.RequestUserChangePassword1;
import io.tech1.framework.b2b.base.security.jwt.domain.dto.requests.RequestUserUpdate1;
import io.tech1.framework.b2b.base.security.jwt.domain.dto.requests.RequestUserUpdate2;
import io.tech1.framework.b2b.base.security.jwt.services.BaseUsersService;
import io.tech1.framework.b2b.base.security.jwt.validators.BaseUsersValidator;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

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
    public void update1(@RequestBody RequestUserUpdate1 requestUserUpdate1) {
        var user = this.currentSessionAssistant.getCurrentJwtUser();
        this.baseUsersValidator.validateUserUpdateRequest1(user.username(), requestUserUpdate1);
        this.baseUsersService.updateUser1(user, requestUserUpdate1);
    }

    @PostMapping("/update2")
    @ResponseStatus(HttpStatus.OK)
    public void update1(@RequestBody RequestUserUpdate2 requestUserUpdate2) {
        var user = this.currentSessionAssistant.getCurrentJwtUser();
        this.baseUsersValidator.validateUserUpdateRequest2(requestUserUpdate2);
        this.baseUsersService.updateUser2(user, requestUserUpdate2);
    }

    @PostMapping("/changePassword1")
    @ResponseStatus(HttpStatus.OK)
    public void changePassword(@RequestBody RequestUserChangePassword1 requestUserChangePassword1) {
        var user = this.currentSessionAssistant.getCurrentJwtUser();
        this.baseUsersValidator.validateUserChangePasswordRequest1(requestUserChangePassword1);
        this.baseUsersService.changePassword1(user, requestUserChangePassword1);
    }
}
