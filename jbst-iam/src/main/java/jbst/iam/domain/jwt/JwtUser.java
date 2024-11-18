package jbst.iam.domain.jwt;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jbst.foundation.domain.base.Email;
import jbst.foundation.domain.base.Password;
import jbst.foundation.domain.base.Username;
import jbst.foundation.domain.constants.ZoneIdsConstants;
import jbst.iam.domain.identifiers.UserId;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.time.ZoneId;
import java.util.*;

import static jbst.foundation.domain.base.AbstractAuthority.*;
import static jbst.foundation.utilities.random.RandomUtility.*;
import static jbst.foundation.utilities.spring.SpringAuthoritiesUtility.getSimpleGrantedAuthorities;

public record JwtUser(
        UserId id,
        Username username,
        Password password,
        ZoneId zoneId,
        Set<SimpleGrantedAuthority> authorities,
        Email email,
        String name,
        boolean passwordChangeRequired,
        Map<String, Object> attributes
) implements UserDetails {

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return this.authorities;
    }

    @Override
    public String getPassword() {
        return this.password.value();
    }

    @Override
    public String getUsername() {
        return this.username.value();
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

    public static JwtUser random() {
        return new JwtUser(
                UserId.random(),
                Username.random(),
                Password.random(),
                randomZoneId(),
                Set.of(
                        new SimpleGrantedAuthority(randomElement(List.of(SUPERADMIN, INVITATION_CODE_READ, INVITATION_CODE_WRITE)))
                ),
                Email.random(),
                randomString(),
                randomBoolean(),
                new HashMap<>(
                        Map.of(
                            randomString(), randomString(),
                            randomString(), randomInteger()
                        )
                )
        );
    }

    public static JwtUser randomSuperadmin() {
        return new JwtUser(
                UserId.random(),
                Username.random(),
                Password.random(),
                randomZoneId(),
                getSimpleGrantedAuthorities(SUPERADMIN),
                Email.random(),
                randomString(),
                false,
                new HashMap<>()
        );
    }

    public static JwtUser testsHardcoded() {
        return testsHardcoded(
                getSimpleGrantedAuthorities("user")
        );
    }

    public static JwtUser testsHardcoded(Set<SimpleGrantedAuthority> authorities) {
        return new JwtUser(
                UserId.testsHardcoded(),
                Username.hardcoded(),
                Password.hardcoded(),
                ZoneIdsConstants.UKRAINE,
                authorities,
                Email.hardcoded(),
                "",
                false,
                new HashMap<>()
        );
    }

    public static JwtUser testsHardcoded(Map<String, Object> attributes) {
        var user = JwtUser.testsHardcoded();
        user.attributes().putAll(attributes);
        return user;
    }

    @JsonIgnore
    public JwtTokenCreationParams getJwtTokenCreationParams() {
        return new JwtTokenCreationParams(
                this.username,
                this.authorities,
                this.zoneId
        );
    }

    @SuppressWarnings("unused")
    public boolean hasAllAuthorities(Set<SimpleGrantedAuthority> authorities) {
        return this.authorities.containsAll(authorities);
    }
}

