package jbst.iam.resources.base;

import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import jbst.iam.annotations.AbstractJbstBaseSecurityResource;
import jbst.iam.assistants.current.CurrentSessionAssistant;
import jbst.iam.domain.dto.requests.RequestNewInvitationParams;
import jbst.iam.domain.dto.responses.ResponseInvitations;
import jbst.iam.domain.identifiers.InvitationId;
import jbst.iam.services.BaseInvitationCodesService;
import jbst.iam.validators.BaseInvitationCodesRequestsValidator;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

// Swagger
@Tag(name = "[jbst] Invitations API")
// Spring
@Slf4j
@AbstractJbstBaseSecurityResource
@RestController
@RequestMapping("/invitations")
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class BaseSecurityInvitationsResource {

    // Assistants
    private final CurrentSessionAssistant currentSessionAssistant;
    // Services
    private final BaseInvitationCodesService baseInvitationCodesService;
    // Validators
    private final BaseInvitationCodesRequestsValidator baseInvitationCodesRequestsValidator;

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public ResponseInvitations findAll() {
        var owner = this.currentSessionAssistant.getCurrentUsername();
        return this.baseInvitationCodesService.findByOwner(owner);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.OK)
    public void save(@RequestBody @Valid RequestNewInvitationParams request) {
        this.baseInvitationCodesRequestsValidator.validateCreateNewInvitationCode(request);
        var owner = this.currentSessionAssistant.getCurrentUsername();
        this.baseInvitationCodesService.save(owner, request);
    }

    @DeleteMapping("/{invitationId}")
    @ResponseStatus(HttpStatus.OK)
    public void deleteById(@PathVariable InvitationId invitationId) {
        var username = this.currentSessionAssistant.getCurrentUsername();
        this.baseInvitationCodesRequestsValidator.validateDeleteById(username, invitationId);
        this.baseInvitationCodesService.deleteById(invitationId);
    }
}

