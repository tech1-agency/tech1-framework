package jbst.iam.repositories.postgres;

import jbst.iam.domain.db.InvitationCode;
import jbst.iam.domain.dto.requests.RequestNewInvitationCodeParams;
import jbst.iam.domain.dto.responses.ResponseInvitationCode;
import jbst.iam.domain.identifiers.InvitationId;
import jbst.iam.repositories.InvitationCodesRepository;
import jbst.iam.domain.postgres.db.PostgresDbInvitationCode;
import jbst.foundation.domain.base.Username;
import jbst.foundation.domain.tuples.TuplePresence;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

import static jbst.iam.domain.db.InvitationCode.INVITATION_CODES_UNUSED;
import static jbst.foundation.utilities.spring.SpringAuthoritiesUtility.getSimpleGrantedAuthorities;
import static jbst.foundation.domain.tuples.TuplePresence.present;
import static java.util.Objects.nonNull;

@SuppressWarnings("JpaQlInspection")
public interface PostgresInvitationCodesRepository extends JpaRepository<PostgresDbInvitationCode, String>, InvitationCodesRepository {
    // ================================================================================================================
    // Any
    // ================================================================================================================
    default TuplePresence<InvitationCode> isPresent(InvitationId invitationId) {
        return this.findById(invitationId.value())
                .map(entity -> present(entity.invitationCode()))
                .orElseGet(TuplePresence::absent);
    }

    default List<ResponseInvitationCode> findResponseCodesByOwner(Username owner) {
        return this.findByOwner(owner).stream()
                .map(PostgresDbInvitationCode::responseInvitationCode)
                .collect(Collectors.toList());
    }

    default InvitationCode findByValueAsAny(String value) {
        var invitationCode = this.findByValue(value);
        return nonNull(invitationCode) ? invitationCode.invitationCode() : null;
    }

    default List<ResponseInvitationCode> findUnused() {
        return this.findByInvitedIsNull(INVITATION_CODES_UNUSED).stream()
                .map(PostgresDbInvitationCode::responseInvitationCode)
                .collect(Collectors.toList());
    }

    long countByOwner(Username username);

    default void delete(InvitationId invitationId) {
        var tuplePresence = this.isPresent(invitationId);
        if (tuplePresence.present()) {
            this.deleteById(invitationId.value());
        }
    }

    default InvitationId saveAs(InvitationCode invitationCode) {
        var entity = this.save(new PostgresDbInvitationCode(invitationCode));
        return entity.invitationCodeId();
    }

    default InvitationId saveAs(Username owner, RequestNewInvitationCodeParams request) {
        var invitationCode = new PostgresDbInvitationCode(
                owner,
                getSimpleGrantedAuthorities(request.authorities())
        );
        var entity = this.save(invitationCode);
        return entity.invitationCodeId();
    }

    // ================================================================================================================
    // Spring Data
    // ================================================================================================================
    List<PostgresDbInvitationCode> findByOwner(Username username);
    List<PostgresDbInvitationCode> findByInvitedIsNull();
    List<PostgresDbInvitationCode> findByInvitedIsNull(Sort sort);
    List<PostgresDbInvitationCode> findByInvitedIsNotNull();
    List<PostgresDbInvitationCode> findByInvitedIsNotNull(Sort sort);
    PostgresDbInvitationCode findByValue(String value);

    @Transactional
    @Modifying
    void deleteByInvitedIsNull();

    @Transactional
    @Modifying
    void deleteByInvitedIsNotNull();
}
