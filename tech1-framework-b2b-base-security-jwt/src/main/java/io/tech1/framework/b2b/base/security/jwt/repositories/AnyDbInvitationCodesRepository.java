package io.tech1.framework.b2b.base.security.jwt.repositories;

import io.tech1.framework.b2b.base.security.jwt.domain.db.AnyDbInvitationCode;
import io.tech1.framework.b2b.base.security.jwt.domain.identifiers.InvitationCodeId;
import io.tech1.framework.domain.base.Username;

public interface AnyDbInvitationCodesRepository {
    AnyDbInvitationCode requirePresence(InvitationCodeId invitationCodeId);
    long countByOwner(Username username);
    AnyDbInvitationCode findByValueAsAny(String value);
}
