package io.tech1.framework.b2b.postgres.security.jwt.services;

import io.tech1.framework.b2b.base.security.jwt.domain.dto.requests.RequestUserRegistration1;
import io.tech1.framework.b2b.base.security.jwt.services.BaseRegistrationService;
import io.tech1.framework.b2b.postgres.security.jwt.domain.db.PostgresDbUser;
import io.tech1.framework.b2b.postgres.security.jwt.repositories.PostgresInvitationCodesRepository;
import io.tech1.framework.b2b.postgres.security.jwt.repositories.PostgresUsersRepository;
import io.tech1.framework.domain.base.Password;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.ZoneId;

@Slf4j
@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class PostgresBaseRegistrationService implements BaseRegistrationService {

    // Repository
    private final PostgresInvitationCodesRepository invitationCodesRepository;
    private final PostgresUsersRepository usersRepository;
    // Password
    private final BCryptPasswordEncoder bCryptPasswordEncoder;

    @Transactional
    @Override
    public void register1(RequestUserRegistration1 requestUserRegistration1) {
        var invitationCode = invitationCodesRepository.findByValue(requestUserRegistration1.invitationCode());

        var hashPassword = this.bCryptPasswordEncoder.encode(requestUserRegistration1.password().value());

        var user = new PostgresDbUser(
                requestUserRegistration1.username(),
                Password.of(hashPassword),
                ZoneId.of(requestUserRegistration1.zoneId()),
                invitationCode.getAuthorities()
        );

        invitationCode.setInvited(user.getUsername());

        this.usersRepository.save(user);
        this.invitationCodesRepository.save(invitationCode);
    }
}
