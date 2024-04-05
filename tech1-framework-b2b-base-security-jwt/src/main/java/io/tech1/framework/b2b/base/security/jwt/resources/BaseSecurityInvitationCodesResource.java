package io.tech1.framework.b2b.base.security.jwt.resources;

import io.tech1.framework.b2b.base.security.jwt.annotations.AbstractFrameworkBaseSecurityResource;
import io.tech1.framework.b2b.base.security.jwt.assistants.current.CurrentSessionAssistant;
import io.tech1.framework.b2b.base.security.jwt.domain.dto.requests.RequestNewInvitationCodeParams;
import io.tech1.framework.b2b.base.security.jwt.domain.dto.responses.ResponseInvitationCodes;
import io.tech1.framework.b2b.base.security.jwt.domain.identifiers.InvitationCodeId;
import io.tech1.framework.b2b.base.security.jwt.services.BaseInvitationCodesService;
import io.tech1.framework.b2b.base.security.jwt.validators.BaseInvitationCodesRequestsValidator;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@SuppressWarnings("SpringJavaInjectionPointsAutowiringInspection")
@Slf4j
@AbstractFrameworkBaseSecurityResource
@RestController
@RequestMapping("/invitationCodes")
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class BaseSecurityInvitationCodesResource {

    // Assistants
    private final CurrentSessionAssistant currentSessionAssistant;
    // Services
    private final BaseInvitationCodesService baseInvitationCodesService;
    // Validators
    private final BaseInvitationCodesRequestsValidator baseInvitationCodesRequestsValidator;

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public ResponseInvitationCodes findAll() {
        var owner = this.currentSessionAssistant.getCurrentUsername();
        return this.baseInvitationCodesService.findByOwner(owner);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.OK)
    public void save(@RequestBody @Valid RequestNewInvitationCodeParams request) {
        this.baseInvitationCodesRequestsValidator.validateCreateNewInvitationCode(request);
        var owner = this.currentSessionAssistant.getCurrentUsername();
        this.baseInvitationCodesService.save(owner, request);
    }

    @DeleteMapping("/{invitationCodeId}")
    @ResponseStatus(HttpStatus.OK)
    public void deleteById(@PathVariable InvitationCodeId invitationCodeId) {
        var username = this.currentSessionAssistant.getCurrentUsername();
        this.baseInvitationCodesRequestsValidator.validateDeleteById(username, invitationCodeId);
        this.baseInvitationCodesService.deleteById(invitationCodeId);
    }
}

