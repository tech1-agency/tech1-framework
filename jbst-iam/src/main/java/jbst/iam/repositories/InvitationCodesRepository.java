package jbst.iam.repositories;

import jbst.iam.domain.db.InvitationCode;
import jbst.iam.domain.dto.requests.RequestNewInvitationCodeParams;
import jbst.iam.domain.dto.responses.ResponseInvitationCode;
import jbst.iam.domain.identifiers.InvitationId;
import jbst.foundation.domain.base.Username;
import jbst.foundation.domain.tuples.TuplePresence;

import java.util.List;

public interface InvitationCodesRepository {
    TuplePresence<InvitationCode> isPresent(InvitationId invitationId);
    List<ResponseInvitationCode> findResponseCodesByOwner(Username owner);
    InvitationCode findByValueAsAny(String value);
    List<ResponseInvitationCode> findUnused();
    long countByOwner(Username username);
    void delete(InvitationId invitationId);
    InvitationId saveAs(InvitationCode invitationCode);
    InvitationId saveAs(Username owner, RequestNewInvitationCodeParams request);
}
