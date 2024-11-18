package jbst.iam.services.mongodb;

import tech1.framework.foundation.domain.properties.ApplicationFrameworkProperties;
import jbst.iam.repositories.mongodb.MongoInvitationCodesRepository;
import jbst.iam.services.abstracts.AbstractBaseInvitationCodesService;
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
