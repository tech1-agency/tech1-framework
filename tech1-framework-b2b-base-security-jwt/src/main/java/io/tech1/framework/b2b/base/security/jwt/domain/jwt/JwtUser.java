package io.tech1.framework.b2b.base.security.jwt.domain.jwt;

import com.fasterxml.jackson.annotation.JsonIgnore;
import io.tech1.framework.b2b.base.security.jwt.domain.identifiers.UserId;
import io.tech1.framework.domain.base.Email;
import io.tech1.framework.domain.base.Password;
import io.tech1.framework.domain.base.Username;
import io.tech1.framework.domain.tests.constants.TestsZoneIdsConstants;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.time.ZoneId;
import java.util.Collection;
import java.util.List;
import java.util.Map;

import static io.tech1.framework.domain.base.AbstractAuthority.*;
import static io.tech1.framework.domain.utilities.random.RandomUtility.*;

public record JwtUser(
        UserId id,
        Username username,
        Password password,
        ZoneId zoneId,
        List<SimpleGrantedAuthority> authorities,
        Email email,
        String name,
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
        return this.username.identifier();
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

    @JsonIgnore
    public JwtTokenCreationParams getJwtTokenCreationParams() {
        return new JwtTokenCreationParams(
                this.username,
                this.authorities,
                this.zoneId
        );
    }

    public static JwtUser random() {
        return new JwtUser(
                UserId.random(),
                Username.random(),
                Password.random(),
                randomZoneId(),
                List.of(
                        new SimpleGrantedAuthority(randomElement(List.of(SUPER_ADMIN, INVITATION_CODE_READ, INVITATION_CODE_WRITE)))
                ),
                Email.random(),
                randomString(),
                Map.of(
                        randomString(), randomString(),
                        randomString(), randomInteger()
                )
        );
    }

    public static JwtUser testsHardcoded() {
        return new JwtUser(
                UserId.testsHardcoded(),
                Username.testsHardcoded(),
                Password.testsHardcoded(),
                TestsZoneIdsConstants.EET_ZONE_ID,
                List.of(),
                Email.testsHardcoded(),
                "",
                Map.of()
        );
    }
}

