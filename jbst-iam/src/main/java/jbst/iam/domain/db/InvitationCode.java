package jbst.iam.domain.db;

import jbst.iam.domain.identifiers.InvitationCodeId;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import tech1.framework.foundation.domain.base.Username;

import java.util.Set;

import static jbst.iam.utilities.SpringAuthoritiesUtility.getSimpleGrantedAuthorities;
import static tech1.framework.foundation.domain.base.AbstractAuthority.SUPERADMIN;
import static tech1.framework.foundation.utilities.random.RandomUtility.randomString;

public record InvitationCode(
        InvitationCodeId id,
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
                InvitationCodeId.random(),
                Username.random(),
                getSimpleGrantedAuthorities(SUPERADMIN),
                randomString(),
                Username.random()
        );
    }

    public static InvitationCode randomNoInvited() {
        return new InvitationCode(
                InvitationCodeId.random(),
                Username.random(),
                getSimpleGrantedAuthorities(SUPERADMIN),
                randomString(),
                null
        );
    }
}
