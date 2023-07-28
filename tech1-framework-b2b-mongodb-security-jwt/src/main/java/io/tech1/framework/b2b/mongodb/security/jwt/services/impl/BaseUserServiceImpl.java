package io.tech1.framework.b2b.mongodb.security.jwt.services.impl;

import io.tech1.framework.b2b.base.security.jwt.domain.dto.requests.RequestUserChangePassword1;
import io.tech1.framework.b2b.base.security.jwt.domain.dto.requests.RequestUserUpdate1;
import io.tech1.framework.b2b.base.security.jwt.domain.dto.requests.RequestUserUpdate2;
import io.tech1.framework.b2b.base.security.jwt.domain.jwt.JwtUser;
import io.tech1.framework.b2b.mongodb.security.jwt.domain.db.DbUser;
import io.tech1.framework.b2b.mongodb.security.jwt.repositories.UserRepository;
import io.tech1.framework.b2b.mongodb.security.jwt.services.BaseUserService;
import io.tech1.framework.domain.base.Password;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.ZoneId;

@SuppressWarnings("SpringJavaInjectionPointsAutowiringInspection")
@Slf4j
@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class BaseUserServiceImpl implements BaseUserService {

    // Repository
    private final UserRepository userRepository;
    // Password
    private final BCryptPasswordEncoder bCryptPasswordEncoder;

    @Override
    public void updateUser1(JwtUser jwtUser, RequestUserUpdate1 requestUserUpdate1) {
        var dbUser = this.userRepository.findByUsername(jwtUser.username());

        dbUser.setZoneId(ZoneId.of(requestUserUpdate1.zoneId()));
        dbUser.setEmail(requestUserUpdate1.email());
        dbUser.setName(requestUserUpdate1.name());
        this.userRepository.save(dbUser);

        this.reauthenticate(dbUser);
    }

    @Override
    public void updateUser2(JwtUser jwtUser, RequestUserUpdate2 requestUserUpdate2) {
        var dbUser = this.userRepository.findByUsername(jwtUser.username());

        dbUser.setZoneId(ZoneId.of(requestUserUpdate2.zoneId()));
        dbUser.setName(requestUserUpdate2.name());
        this.userRepository.save(dbUser);

        this.reauthenticate(dbUser);
    }

    @Override
    public void changePassword1(JwtUser jwtUser, RequestUserChangePassword1 requestUserChangePassword1) {
        var dbUser = this.userRepository.findByUsername(jwtUser.username());

        var hashPassword = this.bCryptPasswordEncoder.encode(requestUserChangePassword1.newPassword().value());

        dbUser.setPassword(Password.of(hashPassword));
        this.userRepository.save(dbUser);

        this.reauthenticate(dbUser);
    }

    // ================================================================================================================
    // PRIVATE METHODS
    // ================================================================================================================
    private void reauthenticate(DbUser dbUser) {
        var jwtUser = dbUser.getJwtUser();
        var authentication = new UsernamePasswordAuthenticationToken(jwtUser, null, jwtUser.getAuthorities());
        SecurityContextHolder.getContext().setAuthentication(authentication);
    }
}
