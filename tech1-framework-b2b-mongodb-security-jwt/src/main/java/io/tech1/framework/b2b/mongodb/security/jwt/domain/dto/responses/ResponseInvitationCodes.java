package io.tech1.framework.b2b.mongodb.security.jwt.domain.dto.responses;

import io.tech1.framework.b2b.mongodb.security.jwt.domain.db.DbInvitationCode;
import lombok.Data;

import java.util.List;
import java.util.Set;

// Lombok
@Data
public class ResponseInvitationCodes {
    private final Set<String> authorities;
    private final List<DbInvitationCode> invitationCodes;
}
