package io.tech1.framework.b2b.base.security.jwt.assistants.userdetails;

import io.tech1.framework.b2b.base.security.jwt.domain.jwt.JwtUser;
import io.tech1.framework.b2b.base.security.jwt.repositories.AnyDbUsersRepository;
import io.tech1.framework.domain.base.Username;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

public abstract class AbstractJwtUserDetailsService implements JwtUserDetailsService {

    // Repository
    protected final AnyDbUsersRepository usersRepository;

    protected AbstractJwtUserDetailsService(AnyDbUsersRepository usersRepository) {
        this.usersRepository = usersRepository;
    }

    @Override
    public JwtUser loadUserByUsername(String username) throws UsernameNotFoundException {
        return this.usersRepository.loadUserByUsername(Username.of(username));
    }
}
