package io.tech1.framework.b2b.base.security.jwt.repositories;

import io.tech1.framework.b2b.base.security.jwt.domain.db.AnyDbInvitationCode;
import io.tech1.framework.b2b.base.security.jwt.domain.dto.requests.RequestNewInvitationCodeParams;
import io.tech1.framework.b2b.base.security.jwt.domain.dto.responses.ResponseInvitationCode;
import io.tech1.framework.b2b.base.security.jwt.domain.identifiers.InvitationCodeId;
import io.tech1.framework.domain.base.Username;
import io.tech1.framework.domain.tuples.TuplePresence;
import org.springframework.data.domain.Sort;

import java.util.List;

public interface AnyDbInvitationCodesRepository {
    TuplePresence<AnyDbInvitationCode> isPresent(InvitationCodeId invitationCodeId);
    List<ResponseInvitationCode> findResponseCodesByOwner(Username owner);
    AnyDbInvitationCode findByValueAsAny(String value);
    List<ResponseInvitationCode> findUnused();
    long countByOwner(Username username);
    void delete(InvitationCodeId invitationCodeId);
    InvitationCodeId saveAs(AnyDbInvitationCode invitationCode);
    InvitationCodeId saveAs(Username owner, RequestNewInvitationCodeParams requestNewInvitationCodeParams);

    default Sort getUnusedSort() {
        return Sort.by("owner").ascending()
                .and(Sort.by("value").ascending());
    }
}
