package io.tech1.framework.b2b.base.security.jwt.services.abstracts;

import io.tech1.framework.b2b.base.security.jwt.domain.dto.requests.RequestUserChangePassword1;
import io.tech1.framework.b2b.base.security.jwt.domain.dto.requests.RequestUserUpdate1;
import io.tech1.framework.b2b.base.security.jwt.domain.dto.requests.RequestUserUpdate2;
import io.tech1.framework.b2b.base.security.jwt.domain.jwt.JwtUser;
import io.tech1.framework.b2b.base.security.jwt.repositories.UsersRepository;
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
    private final UsersRepository usersRepository;
    // Password
    private final BCryptPasswordEncoder bCryptPasswordEncoder;

    @Override
    public void updateUser1(JwtUser user, RequestUserUpdate1 requestUserUpdate1) {
        user = new JwtUser(
                user.id(),
                user.username(),
                user.password(),
                ZoneId.of(requestUserUpdate1.zoneId()),
                user.authorities(),
                requestUserUpdate1.email(),
                requestUserUpdate1.name(),
                user.passwordChangeRequired(),
                user.attributes()
        );
        this.saveAndReauthenticate(user);
    }

    @Override
    public void updateUser2(JwtUser user, RequestUserUpdate2 requestUserUpdate2) {
        user = new JwtUser(
                user.id(),
                user.username(),
                user.password(),
                ZoneId.of(requestUserUpdate2.zoneId()),
                user.authorities(),
                user.email(),
                requestUserUpdate2.name(),
                user.passwordChangeRequired(),
                user.attributes()
        );
        this.saveAndReauthenticate(user);
    }

    @Override
    public void changePassword1(JwtUser user, RequestUserChangePassword1 requestUserChangePassword1) {
        var hashPassword = this.bCryptPasswordEncoder.encode(requestUserChangePassword1.newPassword().value());
        user = new JwtUser(
                user.id(),
                user.username(),
                Password.of(hashPassword),
                user.zoneId(),
                user.authorities(),
                user.email(),
                user.name(),
                user.passwordChangeRequired(),
                user.attributes()
        );
        this.saveAndReauthenticate(user);
    }

    // ================================================================================================================
    // PROTECTED METHODS
    // ================================================================================================================
    protected void saveAndReauthenticate(JwtUser jwtUser) {
        this.usersRepository.saveAs(jwtUser);
        var authentication = new UsernamePasswordAuthenticationToken(jwtUser, null, jwtUser.getAuthorities());
        SecurityContextHolder.getContext().setAuthentication(authentication);
    }
}
