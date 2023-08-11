package io.tech1.framework.b2b.mongodb.security.jwt.repositories;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class MongoFacadeRepositories {
    private final MongoInvitationCodesRepository invitationCodeRepository;
    private final MongoUsersRepository userRepository;
    private final MongoUsersSessionsRepository userSessionRepository;
}
