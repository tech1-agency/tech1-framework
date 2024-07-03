package io.tech1.framework.iam.resources;

import io.swagger.v3.oas.annotations.tags.Tag;
import io.tech1.framework.iam.annotations.AbstractFrameworkBaseSecurityResource;
import io.tech1.framework.iam.assistants.current.CurrentSessionAssistant;
import io.tech1.framework.iam.domain.dto.requests.RequestNewInvitationCodeParams;
import io.tech1.framework.iam.domain.dto.responses.ResponseInvitationCodes;
import io.tech1.framework.iam.domain.identifiers.InvitationCodeId;
import io.tech1.framework.iam.services.BaseInvitationCodesService;
import io.tech1.framework.iam.validators.BaseInvitationCodesRequestsValidator;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;

// Swagger
@Tag(name = "[tech1-framework] InvitationCodes API")
// Spring
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

