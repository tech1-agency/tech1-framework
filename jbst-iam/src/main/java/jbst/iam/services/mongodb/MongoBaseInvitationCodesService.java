package jbst.iam.services.mongodb;

import jbst.iam.repositories.mongodb.MongoInvitationsRepository;
import jbst.iam.services.abstracts.AbstractBaseInvitationCodesService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import jbst.foundation.domain.properties.JbstProperties;

@Service
public class MongoBaseInvitationCodesService extends AbstractBaseInvitationCodesService {

    @Autowired
    public MongoBaseInvitationCodesService(
            MongoInvitationsRepository invitationCodesRepository,
            JbstProperties jbstProperties
    ) {
        super(
                invitationCodesRepository,
                jbstProperties
        );
    }
}
