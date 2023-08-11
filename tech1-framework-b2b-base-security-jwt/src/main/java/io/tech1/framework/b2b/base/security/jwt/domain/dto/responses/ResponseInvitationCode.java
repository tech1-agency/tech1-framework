package io.tech1.framework.b2b.base.security.jwt.domain.dto.responses;

import io.tech1.framework.b2b.base.security.jwt.domain.identifiers.InvitationCodeId;
import io.tech1.framework.domain.base.Username;

import java.util.List;

public record ResponseInvitationCode(
        InvitationCodeId id,
        Username owner,
        List<String> authorities,
        String value,
        Username invited
) {
}
