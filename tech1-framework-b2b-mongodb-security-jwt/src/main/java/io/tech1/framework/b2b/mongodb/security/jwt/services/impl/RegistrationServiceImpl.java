package io.tech1.framework.b2b.mongodb.security.jwt.services.impl;

import io.tech1.framework.b2b.mongodb.security.jwt.domain.db.DbUser;
import io.tech1.framework.b2b.mongodb.security.jwt.domain.dto.requests.RequestUserRegistration1;
import io.tech1.framework.b2b.mongodb.security.jwt.repositories.InvitationCodeRepository;
import io.tech1.framework.b2b.mongodb.security.jwt.repositories.UserRepository;
import io.tech1.framework.b2b.mongodb.security.jwt.services.RegistrationService;
import io.tech1.framework.domain.base.Username;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class RegistrationServiceImpl implements RegistrationService {

    // Repository
    private final InvitationCodeRepository invitationCodeRepository;
    private final UserRepository userRepository;
    // Password
    private final BCryptPasswordEncoder bCryptPasswordEncoder;

    @Override
    public DbUser register1(RequestUserRegistration1 requestUserRegistration1) {
        var invitationCode = invitationCodeRepository.findByValue(requestUserRegistration1.getInvitationCode());

        var hashPassword = this.bCryptPasswordEncoder.encode(requestUserRegistration1.getPassword());

        var user = new DbUser(
                Username.of(requestUserRegistration1.getUsername()),
                hashPassword,
                requestUserRegistration1.getZoneId(),
                invitationCode.getAuthorities()
        );

        user = this.userRepository.save(user);

        invitationCode.editInvited(user.getUsername());
        this.invitationCodeRepository.save(invitationCode);

        return user;
    }
}
