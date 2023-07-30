package io.tech1.framework.b2b.base.security.jwt.services.abstracts;

import io.tech1.framework.b2b.base.security.jwt.domain.db.AnyDbInvitationCode;
import io.tech1.framework.b2b.base.security.jwt.domain.dto.requests.RequestUserRegistration1;
import io.tech1.framework.b2b.base.security.jwt.repositories.AnyDbInvitationCodesRepository;
import io.tech1.framework.b2b.base.security.jwt.repositories.AnyDbUsersRepository;
import io.tech1.framework.b2b.base.security.jwt.services.BaseRegistrationService;
import io.tech1.framework.domain.base.Password;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@AllArgsConstructor(access = AccessLevel.PROTECTED)
public abstract class AbstractBaseRegistrationService implements BaseRegistrationService {

    // Repository
    private final AnyDbInvitationCodesRepository invitationCodesRepository;
    private final AnyDbUsersRepository usersRepository;
    // Password
    private final BCryptPasswordEncoder bCryptPasswordEncoder;

    @Override
    public void register1(RequestUserRegistration1 requestUserRegistration1) {
        var invitationCode = this.invitationCodesRepository.findByValueAsAny(requestUserRegistration1.invitationCode());
        var hashPassword = this.bCryptPasswordEncoder.encode(requestUserRegistration1.password().value());
        invitationCode = new AnyDbInvitationCode(
                invitationCode.id(),
                invitationCode.owner(),
                invitationCode.authorities(),
                invitationCode.value(),
                requestUserRegistration1.username()
        );
        this.usersRepository.saveAs(requestUserRegistration1, Password.of(hashPassword), invitationCode);
        this.invitationCodesRepository.saveAs(invitationCode);
    }
}
