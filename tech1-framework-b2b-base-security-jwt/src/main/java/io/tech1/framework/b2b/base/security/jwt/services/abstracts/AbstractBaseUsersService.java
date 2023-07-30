package io.tech1.framework.b2b.base.security.jwt.services.abstracts;

import io.tech1.framework.b2b.base.security.jwt.domain.dto.requests.RequestUserChangePassword1;
import io.tech1.framework.b2b.base.security.jwt.domain.dto.requests.RequestUserUpdate1;
import io.tech1.framework.b2b.base.security.jwt.domain.dto.requests.RequestUserUpdate2;
import io.tech1.framework.b2b.base.security.jwt.domain.jwt.JwtUser;
import io.tech1.framework.b2b.base.security.jwt.repositories.AnyDbUsersRepository;
import io.tech1.framework.b2b.base.security.jwt.services.BaseUsersService;
import io.tech1.framework.domain.base.Password;
import lombok.AllArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.time.ZoneId;

@AllArgsConstructor
public abstract class AbstractBaseUsersService implements BaseUsersService {

    // Repository
    private final AnyDbUsersRepository usersRepository;
    // Password
    private final BCryptPasswordEncoder bCryptPasswordEncoder;

    @Override
    public void updateUser1(JwtUser jwtUser, RequestUserUpdate1 requestUserUpdate1) {
        jwtUser = new JwtUser(
                jwtUser.id(),
                jwtUser.username(),
                jwtUser.password(),
                ZoneId.of(requestUserUpdate1.zoneId()),
                jwtUser.authorities(),
                requestUserUpdate1.email(),
                requestUserUpdate1.name(),
                jwtUser.attributes()
        );
        this.saveAndReauthenticate(jwtUser);
    }

    @Override
    public void updateUser2(JwtUser jwtUser, RequestUserUpdate2 requestUserUpdate2) {
        jwtUser = new JwtUser(
                jwtUser.id(),
                jwtUser.username(),
                jwtUser.password(),
                ZoneId.of(requestUserUpdate2.zoneId()),
                jwtUser.authorities(),
                jwtUser.email(),
                requestUserUpdate2.name(),
                jwtUser.attributes()
        );
        this.saveAndReauthenticate(jwtUser);
    }

    @Override
    public void changePassword1(JwtUser jwtUser, RequestUserChangePassword1 requestUserChangePassword1) {
        var hashPassword = this.bCryptPasswordEncoder.encode(requestUserChangePassword1.newPassword().value());
        jwtUser = new JwtUser(
                jwtUser.id(),
                jwtUser.username(),
                Password.of(hashPassword),
                jwtUser.zoneId(),
                jwtUser.authorities(),
                jwtUser.email(),
                jwtUser.name(),
                jwtUser.attributes()
        );
        this.saveAndReauthenticate(jwtUser);
    }

    // ================================================================================================================
    // PROTECTED METHODS
    // ================================================================================================================
    protected void saveAndReauthenticate(JwtUser jwtUser) {
        this.usersRepository.saveAsJwtUser(jwtUser);
        var authentication = new UsernamePasswordAuthenticationToken(jwtUser, null, jwtUser.getAuthorities());
        SecurityContextHolder.getContext().setAuthentication(authentication);
    }
}
