package io.tech1.framework.b2b.mongodb.security.jwt.domain.dto.responses;

import io.tech1.framework.b2b.mongodb.security.jwt.domain.db.DbInvitationCode;
import io.tech1.framework.domain.base.Username;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.List;
import java.util.stream.Collectors;

import static io.tech1.framework.domain.asserts.Asserts.assertNonNullOrThrow;
import static io.tech1.framework.domain.utilities.exceptions.ExceptionsMessagesUtility.invalidAttribute;

// Lombok
@Getter
@EqualsAndHashCode
@ToString
public class ResponseInvitationCode1 {
    private final Username owner;
    private final String value;
    private final List<String> authorities;

    public ResponseInvitationCode1(
            DbInvitationCode invitationCode
    ) {
        assertNonNullOrThrow(invitationCode, invalidAttribute("ResponseInvitationCode1.invitationCode"));
        this.owner = invitationCode.getOwner();
        this.value = invitationCode.getValue();
        this.authorities = invitationCode.getAuthorities().stream().map(SimpleGrantedAuthority::getAuthority).collect(Collectors.toList());
    }
}
