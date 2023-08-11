package io.tech1.framework.b2b.base.security.jwt.domain.dto.responses;

import java.util.List;
import java.util.Set;

public record ResponseInvitationCodes(
        Set<String> authorities,
        List<ResponseInvitationCode> invitationCodes
) {
}
