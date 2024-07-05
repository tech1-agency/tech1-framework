package io.tech1.framework.iam.domain.dto.responses;

import java.util.List;
import java.util.Set;

public record ResponseInvitationCodes(
        Set<String> authorities,
        List<ResponseInvitationCode> invitationCodes
) {
}
