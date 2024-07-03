package io.tech1.framework.b2b.mongodb.security.jwt.repositories;

import io.tech1.framework.iam.domain.db.InvitationCode;
import io.tech1.framework.iam.domain.dto.requests.RequestNewInvitationCodeParams;
import io.tech1.framework.iam.domain.dto.responses.ResponseInvitationCode;
import io.tech1.framework.iam.domain.identifiers.InvitationCodeId;
import io.tech1.framework.iam.repositories.InvitationCodesRepository;
import io.tech1.framework.b2b.mongodb.security.jwt.domain.db.MongoDbInvitationCode;
import io.tech1.framework.foundation.domain.base.Username;
import io.tech1.framework.foundation.domain.tuples.TuplePresence;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.stream.Collectors;

import static io.tech1.framework.iam.comparators.SecurityJwtSorts.INVITATION_CODES_UNUSED;
import static io.tech1.framework.iam.utilities.SpringAuthoritiesUtility.getSimpleGrantedAuthorities;
import static io.tech1.framework.foundation.domain.tuples.TuplePresence.present;
import static java.util.Objects.nonNull;

@Repository
public interface MongoInvitationCodesRepository extends MongoRepository<MongoDbInvitationCode, String>, InvitationCodesRepository {
    // ================================================================================================================
    // Any
    // ================================================================================================================
    default TuplePresence<InvitationCode> isPresent(InvitationCodeId invitationCodeId) {
        return this.findById(invitationCodeId.value())
                .map(entity -> present(entity.invitationCode()))
                .orElseGet(TuplePresence::absent);
    }

    default List<ResponseInvitationCode> findResponseCodesByOwner(Username owner) {
        return this.findByOwner(owner).stream()
                .map(MongoDbInvitationCode::responseInvitationCode)
                .collect(Collectors.toList());
    }

    default InvitationCode findByValueAsAny(String value) {
        var invitationCode = this.findByValue(value);
        return nonNull(invitationCode) ? invitationCode.invitationCode() : null;
    }

    default List<ResponseInvitationCode> findUnused() {
        return this.findByInvitedIsNull(INVITATION_CODES_UNUSED).stream()
                .map(MongoDbInvitationCode::responseInvitationCode)
                .collect(Collectors.toList());
    }

    long countByOwner(Username username);

    default void delete(InvitationCodeId invitationCodeId) {
        this.deleteById(invitationCodeId.value());
    }

    default InvitationCodeId saveAs(InvitationCode invitationCode) {
        var entity = this.save(new MongoDbInvitationCode(invitationCode));
        return entity.invitationCodeId();
    }

    default InvitationCodeId saveAs(Username owner, RequestNewInvitationCodeParams request) {
        var invitationCode = new MongoDbInvitationCode(
                owner,
                getSimpleGrantedAuthorities(request.authorities())
        );
        var entity = this.save(invitationCode);
        return entity.invitationCodeId();
    }

    // ================================================================================================================
    // Spring Data
    // ================================================================================================================
    List<MongoDbInvitationCode> findByOwner(Username username);
    List<MongoDbInvitationCode> findByInvitedIsNull();
    List<MongoDbInvitationCode> findByInvitedIsNull(Sort sort);
    List<MongoDbInvitationCode> findByInvitedIsNotNull();
    List<MongoDbInvitationCode> findByInvitedIsNotNull(Sort sort);
    MongoDbInvitationCode findByValue(String value);

    void deleteByInvitedIsNull();
    void deleteByInvitedIsNotNull();
}
