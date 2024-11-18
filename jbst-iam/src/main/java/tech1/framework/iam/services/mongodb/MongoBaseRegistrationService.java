package tech1.framework.iam.services.mongodb;

import tech1.framework.iam.repositories.mongodb.MongoInvitationCodesRepository;
import tech1.framework.iam.repositories.mongodb.MongoUsersRepository;
import tech1.framework.iam.services.abstracts.AbstractBaseRegistrationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class MongoBaseRegistrationService extends AbstractBaseRegistrationService {

    @Autowired
    public MongoBaseRegistrationService(
            MongoInvitationCodesRepository invitationCodesRepository,
            MongoUsersRepository usersRepository,
            BCryptPasswordEncoder bCryptPasswordEncoder
    ) {
        super(
                invitationCodesRepository,
                usersRepository,
                bCryptPasswordEncoder
        );
    }
}
