package io.tech1.framework.b2b.mongodb.security.jwt.services.impl;

import io.tech1.framework.b2b.mongodb.security.jwt.assistants.core.CurrentSessionAssistant;
import io.tech1.framework.b2b.mongodb.security.jwt.domain.dto.requests.RequestUserChangePassword1;
import io.tech1.framework.b2b.mongodb.security.jwt.domain.dto.requests.RequestUserUpdate1;
import io.tech1.framework.b2b.mongodb.security.jwt.domain.dto.requests.RequestUserUpdate2;
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

@Slf4j
@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class BaseUserServiceImpl implements BaseUserService {

    // Assistants
    private final CurrentSessionAssistant currentSessionAssistant;
    // Repository
    private final UserRepository userRepository;
    // Password
    private final BCryptPasswordEncoder bCryptPasswordEncoder;

    @Override
    public void updateUser1(RequestUserUpdate1 requestUserUpdate1) {
        var currentJwtUser = this.currentSessionAssistant.getCurrentJwtUser();
        var currentDbUser = currentJwtUser.getDbUser();

        currentDbUser.edit1(requestUserUpdate1);
        this.userRepository.save(currentDbUser);

        var authentication = new UsernamePasswordAuthenticationToken(currentJwtUser, null, currentJwtUser.getAuthorities());
        SecurityContextHolder.getContext().setAuthentication(authentication);
    }

    @Override
    public void updateUser2(RequestUserUpdate2 requestUserUpdate2) {
        var currentJwtUser = this.currentSessionAssistant.getCurrentJwtUser();
        var currentDbUser = currentJwtUser.getDbUser();

        currentDbUser.edit2(requestUserUpdate2);
        this.userRepository.save(currentDbUser);

        var authentication = new UsernamePasswordAuthenticationToken(currentJwtUser, null, currentJwtUser.getAuthorities());
        SecurityContextHolder.getContext().setAuthentication(authentication);
    }

    @Override
    public void changePassword1(RequestUserChangePassword1 requestUserChangePassword1) {
        var currentJwtUser = this.currentSessionAssistant.getCurrentJwtUser();
        var currentDbUser = currentJwtUser.getDbUser();

        var hashPassword = this.bCryptPasswordEncoder.encode(requestUserChangePassword1.getNewPassword());

        currentDbUser.changePassword(Password.of(hashPassword));
        this.userRepository.save(currentDbUser);

        var authentication = new UsernamePasswordAuthenticationToken(currentJwtUser, null, currentJwtUser.getAuthorities());
        SecurityContextHolder.getContext().setAuthentication(authentication);
    }
}
