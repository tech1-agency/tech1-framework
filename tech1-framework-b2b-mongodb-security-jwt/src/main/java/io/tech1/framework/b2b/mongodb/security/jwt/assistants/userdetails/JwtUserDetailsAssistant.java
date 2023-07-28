package io.tech1.framework.b2b.mongodb.security.jwt.assistants.userdetails;

import io.tech1.framework.b2b.mongodb.security.jwt.domain.jwt.JwtUser;
import io.tech1.framework.b2b.mongodb.security.jwt.repositories.UserRepository;
import io.tech1.framework.domain.base.Username;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import static io.tech1.framework.domain.utilities.exceptions.ExceptionsMessagesUtility.entityNotFound;
import static java.util.Objects.nonNull;

@SuppressWarnings("SpringJavaInjectionPointsAutowiringInspection")
@Slf4j
@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class JwtUserDetailsAssistant implements UserDetailsService {

    // Repository
    private final UserRepository userRepository;

    @Override
    public JwtUser loadUserByUsername(String username) throws UsernameNotFoundException {
        var user = this.userRepository.findByUsername(Username.of(username));
        if (nonNull(user)) {
            return new JwtUser(
                    user.getUsername(),
                    user.getPassword(),
                    user.getAuthorities(),
                    user.getEmail(),
                    user.getName(),
                    user.getZoneId(),
                    user.getAttributes()
            );
        } else {
            throw new UsernameNotFoundException(entityNotFound("Username", username));
        }
    }
}
