package tech1.framework.iam.assistants.userdetails;

import tech1.framework.iam.domain.jwt.JwtUser;
import tech1.framework.iam.repositories.UsersRepository;
import tech1.framework.foundation.domain.base.Username;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

@AllArgsConstructor(access = AccessLevel.PROTECTED)
public abstract class AbstractJwtUserDetailsService implements JwtUserDetailsService {

    // Repository
    protected final UsersRepository usersRepository;

    @Override
    public JwtUser loadUserByUsername(String username) throws UsernameNotFoundException {
        return this.usersRepository.loadUserByUsername(Username.of(username));
    }
}
