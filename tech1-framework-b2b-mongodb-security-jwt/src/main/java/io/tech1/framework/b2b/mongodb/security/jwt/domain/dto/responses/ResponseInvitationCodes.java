package io.tech1.framework.b2b.mongodb.security.jwt.domain.dto.responses;

import io.tech1.framework.b2b.mongodb.security.jwt.domain.db.DbInvitationCode;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

import java.util.List;
import java.util.Set;

// Lombok
@AllArgsConstructor
@Getter
@EqualsAndHashCode
@ToString
public class ResponseInvitationCodes {
    private final Set<String> authorities;
    private final List<DbInvitationCode> invitationCodes;
}
