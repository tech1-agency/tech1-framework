package io.tech1.framework.b2b.mongodb.security.jwt.domain.jwt;

import io.tech1.framework.b2b.mongodb.security.jwt.domain.db.DbUser;
import lombok.Data;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;

// Lombok
@Data
public class JwtUser implements UserDetails {

    private final DbUser dbUser;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return this.dbUser.getAuthorities();
    }

    @Override
    public String getPassword() {
        return this.dbUser.getPassword().getValue();
    }

    @Override
    public String getUsername() {
        return this.dbUser.getUsername().getIdentifier();
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}

