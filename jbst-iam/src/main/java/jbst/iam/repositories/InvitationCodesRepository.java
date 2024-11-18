package jbst.iam.repositories;

import jbst.iam.domain.db.InvitationCode;
import jbst.iam.domain.dto.requests.RequestNewInvitationCodeParams;
import jbst.iam.domain.dto.responses.ResponseInvitationCode;
import jbst.iam.domain.identifiers.InvitationCodeId;
import tech1.framework.foundation.domain.base.Username;
import tech1.framework.foundation.domain.tuples.TuplePresence;

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
