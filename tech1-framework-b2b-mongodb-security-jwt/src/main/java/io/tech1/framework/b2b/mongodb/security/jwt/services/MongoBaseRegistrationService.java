package io.tech1.framework.b2b.mongodb.security.jwt.services;

import io.tech1.framework.b2b.base.security.jwt.services.abstracts.AbstractBaseRegistrationService;
import io.tech1.framework.b2b.mongodb.security.jwt.repositories.MongoInvitationCodesRepository;
import io.tech1.framework.b2b.mongodb.security.jwt.repositories.MongoUsersRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@SuppressWarnings("SpringJavaInjectionPointsAutowiringInspection")
@Slf4j
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
