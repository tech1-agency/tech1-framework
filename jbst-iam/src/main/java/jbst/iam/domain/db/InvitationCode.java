package jbst.iam.domain.db;

import jbst.iam.domain.identifiers.InvitationId;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import jbst.foundation.domain.base.Username;

import java.util.Set;

import static jbst.foundation.utilities.spring.SpringAuthoritiesUtility.getSimpleGrantedAuthorities;
import static jbst.foundation.domain.base.AbstractAuthority.SUPERADMIN;
import static jbst.foundation.utilities.random.RandomUtility.randomString;

public record InvitationCode(
        InvitationId id,
        Username owner,
        Set<SimpleGrantedAuthority> authorities,
        String value,
        Username invited
) {

    public static final Sort INVITATION_CODES_UNUSED = Sort.by("owner").ascending()
            .and(Sort.by("value").ascending());

    public static final int DEFAULT_INVITATION_CODE_LENGTH = 40;

    public static InvitationCode random() {
        return new InvitationCode(
                InvitationId.random(),
                Username.random(),
                getSimpleGrantedAuthorities(SUPERADMIN),
                randomString(),
                Username.random()
        );
    }

    public static InvitationCode randomNoInvited() {
        return new InvitationCode(
                InvitationId.random(),
                Username.random(),
                getSimpleGrantedAuthorities(SUPERADMIN),
                randomString(),
                null
        );
    }
}
