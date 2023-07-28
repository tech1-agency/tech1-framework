package io.tech1.framework.b2b.mongodb.security.jwt.resources;


import io.tech1.framework.b2b.base.security.jwt.annotations.AbstractFrameworkBaseSecurityResource;
import io.tech1.framework.b2b.base.security.jwt.domain.dto.requests.RequestNewInvitationCodeParams;
import io.tech1.framework.b2b.mongodb.security.jwt.assistants.core.CurrentSessionAssistant;
import io.tech1.framework.b2b.mongodb.security.jwt.domain.dto.responses.ResponseInvitationCodes;
import io.tech1.framework.b2b.mongodb.security.jwt.services.InvitationCodeService;
import io.tech1.framework.b2b.base.security.jwt.validators.InvitationCodeRequestsValidator;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@Slf4j
@AbstractFrameworkBaseSecurityResource
@RestController
@RequestMapping("/invitationCodes")
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class BaseSecurityInvitationCodesResource {

    // Assistants
    private final CurrentSessionAssistant currentSessionAssistant;
    // Services
    private final InvitationCodeService invitationCodeService;
    // Validators
    private final InvitationCodeRequestsValidator invitationCodeRequestsValidator;

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public ResponseInvitationCodes findAll() {
        var owner = this.currentSessionAssistant.getCurrentUsername();
        return this.invitationCodeService.findByOwner(owner);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.OK)
    public void save(@RequestBody RequestNewInvitationCodeParams requestNewInvitationCodeParams) {
        var owner = this.currentSessionAssistant.getCurrentUsername();
        this.invitationCodeRequestsValidator.validateCreateNewInvitationCode(requestNewInvitationCodeParams);
        this.invitationCodeService.save(requestNewInvitationCodeParams, owner);
    }

    @DeleteMapping("/{invitationCodeId}")
    @ResponseStatus(HttpStatus.OK)
    public void deleteById(@PathVariable String invitationCodeId) {
        var username = this.currentSessionAssistant.getCurrentUsername();
        this.invitationCodeRequestsValidator.validateDeleteById(username, invitationCodeId);
        this.invitationCodeService.deleteById(invitationCodeId);
    }
}

