package io.tech1.framework.b2b.base.security.jwt.domain.db;

import io.tech1.framework.b2b.base.security.jwt.domain.identifiers.InvitationCodeId;
import io.tech1.framework.domain.base.Username;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.Set;

import static io.tech1.framework.b2b.base.security.jwt.utilities.SpringAuthoritiesUtility.getSimpleGrantedAuthorities;
import static io.tech1.framework.domain.base.AbstractAuthority.SUPER_ADMIN;
import static io.tech1.framework.domain.utilities.random.RandomUtility.randomString;

public record InvitationCode(
        InvitationCodeId id,
        Username owner,
        Set<SimpleGrantedAuthority> authorities,
        String value,
        Username invited
) {

    public static InvitationCode random() {
        return new InvitationCode(
                InvitationCodeId.random(),
                Username.random(),
                getSimpleGrantedAuthorities(SUPER_ADMIN),
                randomString(),
                Username.random()
        );
    }

    public static InvitationCode randomNoInvited() {
        return new InvitationCode(
                InvitationCodeId.random(),
                Username.random(),
                getSimpleGrantedAuthorities(SUPER_ADMIN),
                randomString(),
                null
        );
    }
}
