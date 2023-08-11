package io.tech1.framework.b2b.base.security.jwt.resources;

import io.tech1.framework.b2b.base.security.jwt.annotations.AbstractFrameworkBaseSecurityResource;
import io.tech1.framework.b2b.base.security.jwt.domain.dto.requests.RequestUserRegistration1;
import io.tech1.framework.b2b.base.security.jwt.domain.events.EventRegistration1;
import io.tech1.framework.b2b.base.security.jwt.events.publishers.SecurityJwtIncidentPublisher;
import io.tech1.framework.b2b.base.security.jwt.events.publishers.SecurityJwtPublisher;
import io.tech1.framework.b2b.base.security.jwt.services.BaseRegistrationService;
import io.tech1.framework.b2b.base.security.jwt.validators.BaseRegistrationRequestsValidator;
import io.tech1.framework.domain.exceptions.authentication.RegistrationException;
import io.tech1.framework.incidents.domain.registration.IncidentRegistration1;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@SuppressWarnings("SpringJavaInjectionPointsAutowiringInspection")
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
    public void register1(@RequestBody RequestUserRegistration1 requestUserRegistration1) throws RegistrationException {
        this.baseRegistrationRequestsValidator.validateRegistrationRequest1(requestUserRegistration1);
        this.baseRegistrationService.register1(requestUserRegistration1);
        this.securityJwtPublisher.publishRegistration1(new EventRegistration1(requestUserRegistration1));
        this.securityJwtIncidentPublisher.publishRegistration1(new IncidentRegistration1(requestUserRegistration1.username()));
    }
}
