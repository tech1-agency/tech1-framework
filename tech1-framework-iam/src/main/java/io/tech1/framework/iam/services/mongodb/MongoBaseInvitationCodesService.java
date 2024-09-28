package io.tech1.framework.iam.services.mongodb;

import tech1.framework.foundation.domain.properties.ApplicationFrameworkProperties;
import io.tech1.framework.iam.repositories.mongodb.MongoInvitationCodesRepository;
import io.tech1.framework.iam.services.abstracts.AbstractBaseInvitationCodesService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class MongoBaseInvitationCodesService extends AbstractBaseInvitationCodesService {

    @Autowired
    public MongoBaseInvitationCodesService(
            MongoInvitationCodesRepository invitationCodesRepository,
            ApplicationFrameworkProperties applicationFrameworkProperties
    ) {
        super(
                invitationCodesRepository,
                applicationFrameworkProperties
        );
    }
}
