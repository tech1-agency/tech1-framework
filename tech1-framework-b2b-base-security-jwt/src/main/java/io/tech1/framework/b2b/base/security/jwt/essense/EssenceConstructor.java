package io.tech1.framework.b2b.base.security.jwt.essense;

import io.tech1.framework.domain.properties.base.DefaultUser;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.List;

public interface EssenceConstructor {
    long saveDefaultUsers(List<DefaultUser> defaultUsers);
    void saveInvitationCodes(DefaultUser defaultUser, List<SimpleGrantedAuthority> authorities);
}
