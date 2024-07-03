package io.tech1.framework.b2b.base.security.jwt.assistants.userdetails;

import io.tech1.framework.b2b.base.security.jwt.domain.jwt.JwtUser;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

public interface JwtUserDetailsService extends UserDetailsService {
    JwtUser loadUserByUsername(String username) throws UsernameNotFoundException;
}
