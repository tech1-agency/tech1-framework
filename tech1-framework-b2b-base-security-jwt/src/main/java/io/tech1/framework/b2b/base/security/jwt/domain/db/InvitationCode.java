package io.tech1.framework.b2b.base.security.jwt.domain.db;

import io.tech1.framework.b2b.base.security.jwt.domain.identifiers.InvitationCodeId;
import io.tech1.framework.domain.base.Username;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.List;

public record InvitationCode(
        InvitationCodeId id,
        Username owner,
        List<SimpleGrantedAuthority> authorities,
        String value,
        Username invited
) {
}
