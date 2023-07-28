package io.tech1.framework.b2b.mongodb.security.jwt.assistants.userdetails;

import io.tech1.framework.b2b.base.security.jwt.assistants.userdetails.JwtUserDetailsService;
import io.tech1.framework.b2b.base.security.jwt.domain.jwt.JwtUser;
import io.tech1.framework.b2b.mongodb.security.jwt.repositories.MongoUsersRepository;
import io.tech1.framework.domain.base.Username;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import static io.tech1.framework.domain.utilities.exceptions.ExceptionsMessagesUtility.entityNotFound;
import static java.util.Objects.nonNull;

@SuppressWarnings("SpringJavaInjectionPointsAutowiringInspection")
@Slf4j
@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class MongoUserDetailsAssistant implements JwtUserDetailsService {

    // Repository
    private final MongoUsersRepository mongoUsersRepository;

    @Override
    public JwtUser loadUserByUsername(String username) throws UsernameNotFoundException {
        var user = this.mongoUsersRepository.findByUsername(Username.of(username));
        if (nonNull(user)) {
            return user.getJwtUser();
        } else {
            throw new UsernameNotFoundException(entityNotFound("Username", username));
        }
    }
}
