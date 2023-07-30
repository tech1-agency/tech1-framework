package io.tech1.framework.b2b.postgres.security.jwt.services;

import io.tech1.framework.b2b.base.security.jwt.domain.dto.requests.RequestUserChangePassword1;
import io.tech1.framework.b2b.base.security.jwt.domain.dto.requests.RequestUserUpdate1;
import io.tech1.framework.b2b.base.security.jwt.domain.dto.requests.RequestUserUpdate2;
import io.tech1.framework.b2b.base.security.jwt.domain.jwt.JwtUser;
import io.tech1.framework.b2b.base.security.jwt.services.abstracts.AbstractBaseUsersService;
import io.tech1.framework.b2b.postgres.security.jwt.repositories.PostgresUsersRepository;
import io.tech1.framework.domain.base.Password;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.ZoneId;

@Slf4j
@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class PostgresBaseUsersService extends AbstractBaseUsersService {

    // Repository
    private final PostgresUsersRepository usersRepository;
    // Password
    private final BCryptPasswordEncoder bCryptPasswordEncoder;

    @Override
    public void updateUser1(JwtUser jwtUser, RequestUserUpdate1 requestUserUpdate1) {
        var dbUser = this.usersRepository.findByUsername(jwtUser.username());

        dbUser.setZoneId(ZoneId.of(requestUserUpdate1.zoneId()));
        dbUser.setEmail(requestUserUpdate1.email());
        dbUser.setName(requestUserUpdate1.name());
        this.usersRepository.save(dbUser);

        this.reauthenticate(dbUser.getJwtUser());
    }

    @Override
    public void updateUser2(JwtUser jwtUser, RequestUserUpdate2 requestUserUpdate2) {
        var dbUser = this.usersRepository.findByUsername(jwtUser.username());

        dbUser.setZoneId(ZoneId.of(requestUserUpdate2.zoneId()));
        dbUser.setName(requestUserUpdate2.name());
        this.usersRepository.save(dbUser);

        this.reauthenticate(dbUser.getJwtUser());
    }

    @Override
    public void changePassword1(JwtUser jwtUser, RequestUserChangePassword1 requestUserChangePassword1) {
        var dbUser = this.usersRepository.findByUsername(jwtUser.username());

        var hashPassword = this.bCryptPasswordEncoder.encode(requestUserChangePassword1.newPassword().value());

        dbUser.setPassword(Password.of(hashPassword));
        this.usersRepository.save(dbUser);

        this.reauthenticate(dbUser.getJwtUser());
    }
}
