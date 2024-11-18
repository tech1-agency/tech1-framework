package tech1.framework.iam.resources.base;

import io.swagger.v3.oas.annotations.tags.Tag;
import tech1.framework.foundation.domain.exceptions.authentication.RegistrationException;
import tech1.framework.foundation.incidents.domain.registration.IncidentRegistration1;
import tech1.framework.iam.annotations.AbstractFrameworkBaseSecurityResource;
import tech1.framework.iam.domain.dto.requests.RequestUserRegistration1;
import tech1.framework.iam.domain.events.EventRegistration1;
import tech1.framework.iam.events.publishers.SecurityJwtIncidentPublisher;
import tech1.framework.iam.events.publishers.SecurityJwtPublisher;
import tech1.framework.iam.services.BaseRegistrationService;
import tech1.framework.iam.validators.BaseRegistrationRequestsValidator;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

// Swagger
@Tag(name = "[tech1-framework] Registration API")
// Spring
@Slf4j
@AbstractFrameworkBaseSecurityResource
@RestController
@RequestMapping("/registration")
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class BaseSecurityRegistrationResource {

    // Services
    private final BaseRegistrationService baseRegistrationService;
    // Publishers
    private final SecurityJwtPublisher securityJwtPublisher;
    private final SecurityJwtIncidentPublisher securityJwtIncidentPublisher;
    // Validators
    private final BaseRegistrationRequestsValidator baseRegistrationRequestsValidator;

    @PostMapping("/register1")
    @ResponseStatus(HttpStatus.OK)
    public void register1(@RequestBody @Valid RequestUserRegistration1 request) throws RegistrationException {
        request = request.createReworkedUkraineZoneId();
        this.baseRegistrationRequestsValidator.validateRegistrationRequest1(request);
        this.baseRegistrationService.register1(request);
        this.securityJwtPublisher.publishRegistration1(new EventRegistration1(request));
        this.securityJwtIncidentPublisher.publishRegistration1(new IncidentRegistration1(request.username()));
    }
}
