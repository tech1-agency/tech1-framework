package io.tech1.framework.b2b.mongodb.security.jwt.domain.dto.responses;

import io.tech1.framework.b2b.mongodb.security.jwt.domain.db.DbInvitationCode;

import java.util.List;
import java.util.Set;

public record ResponseInvitationCodes(
        Set<String> authorities,
        List<DbInvitationCode> invitationCodes
) {
}
