package io.tech1.framework.b2b.base.security.jwt.essense;

import io.tech1.framework.domain.properties.base.DefaultUser;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.List;

public interface EssenceConstructor {
    boolean noDefaultUsers();
    long saveDefaultUsers(List<DefaultUser> defaultUsers);

    boolean noInvitationCodes(DefaultUser defaultUser);
    void saveInvitationCodes(DefaultUser defaultUser, List<SimpleGrantedAuthority> authorities);
}
