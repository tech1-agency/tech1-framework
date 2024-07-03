package io.tech1.framework.iam.assistants.userdetails;

import io.tech1.framework.iam.domain.jwt.JwtUser;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

public interface JwtUserDetailsService extends UserDetailsService {
    JwtUser loadUserByUsername(String username) throws UsernameNotFoundException;
}
