package io.tech1.framework.b2b.postgres.security.jwt.repositories;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class PostgresFacadeRepositories {
    private final PostgresInvitationCodesRepository invitationCodeRepository;
    private final PostgresUsersRepository userRepository;
    private final PostgresUsersSessionsRepository userSessionRepository;
}
