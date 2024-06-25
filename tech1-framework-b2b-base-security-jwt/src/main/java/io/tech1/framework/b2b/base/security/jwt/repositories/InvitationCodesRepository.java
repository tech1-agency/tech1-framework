package io.tech1.framework.b2b.base.security.jwt.repositories;

import io.tech1.framework.b2b.base.security.jwt.domain.db.InvitationCode;
import io.tech1.framework.b2b.base.security.jwt.domain.dto.requests.RequestNewInvitationCodeParams;
import io.tech1.framework.b2b.base.security.jwt.domain.dto.responses.ResponseInvitationCode;
import io.tech1.framework.b2b.base.security.jwt.domain.identifiers.InvitationCodeId;
import io.tech1.framework.foundation.domain.base.Username;
import io.tech1.framework.foundation.domain.tuples.TuplePresence;

import java.util.List;

public interface InvitationCodesRepository {
    TuplePresence<InvitationCode> isPresent(InvitationCodeId invitationCodeId);
    List<ResponseInvitationCode> findResponseCodesByOwner(Username owner);
    InvitationCode findByValueAsAny(String value);
    List<ResponseInvitationCode> findUnused();
    long countByOwner(Username username);
    void delete(InvitationCodeId invitationCodeId);
    InvitationCodeId saveAs(InvitationCode invitationCode);
    InvitationCodeId saveAs(Username owner, RequestNewInvitationCodeParams request);
}
