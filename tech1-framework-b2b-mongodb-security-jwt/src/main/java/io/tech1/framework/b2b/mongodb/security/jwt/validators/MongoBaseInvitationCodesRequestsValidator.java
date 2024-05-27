package io.tech1.framework.b2b.mongodb.security.jwt.validators;

import io.tech1.framework.b2b.base.security.jwt.validators.abtracts.AbstractBaseInvitationCodesRequestsValidator;
import io.tech1.framework.b2b.mongodb.security.jwt.repositories.MongoInvitationCodesRepository;
import io.tech1.framework.domain.properties.ApplicationFrameworkProperties;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@SuppressWarnings("SpringJavaInjectionPointsAutowiringInspection")
@Slf4j
@Component
public class MongoBaseInvitationCodesRequestsValidator extends AbstractBaseInvitationCodesRequestsValidator {

    public MongoBaseInvitationCodesRequestsValidator(
            MongoInvitationCodesRepository invitationCodesRepository,
            ApplicationFrameworkProperties applicationFrameworkProperties
    ) {
        super(
                invitationCodesRepository,
                applicationFrameworkProperties
        );
    }
}
