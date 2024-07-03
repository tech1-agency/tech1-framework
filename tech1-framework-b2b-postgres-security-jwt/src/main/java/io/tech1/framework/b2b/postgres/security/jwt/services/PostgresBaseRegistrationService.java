package io.tech1.framework.b2b.postgres.security.jwt.services;

import io.tech1.framework.iam.domain.dto.requests.RequestUserRegistration1;
import io.tech1.framework.iam.services.abstracts.AbstractBaseRegistrationService;
import io.tech1.framework.b2b.postgres.security.jwt.repositories.PostgresInvitationCodesRepository;
import io.tech1.framework.b2b.postgres.security.jwt.repositories.PostgresUsersRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
public class PostgresBaseRegistrationService extends AbstractBaseRegistrationService {

    @Autowired
    public PostgresBaseRegistrationService(
            PostgresInvitationCodesRepository invitationCodesRepository,
            PostgresUsersRepository usersRepository,
            BCryptPasswordEncoder bCryptPasswordEncoder
    ) {
        super(
                invitationCodesRepository,
                usersRepository,
                bCryptPasswordEncoder
        );
    }

    @Transactional
    @Override
    public void register1(RequestUserRegistration1 requestUserRegistration1) {
        super.register1(requestUserRegistration1);
    }
}
