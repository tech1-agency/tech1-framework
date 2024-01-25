package io.tech1.framework.b2b.postgres.security.jwt.repositories;

import io.tech1.framework.b2b.base.security.jwt.domain.db.InvitationCode;
import io.tech1.framework.b2b.base.security.jwt.domain.dto.requests.RequestNewInvitationCodeParams;
import io.tech1.framework.b2b.base.security.jwt.domain.dto.responses.ResponseInvitationCode;
import io.tech1.framework.b2b.base.security.jwt.domain.identifiers.InvitationCodeId;
import io.tech1.framework.b2b.base.security.jwt.repositories.InvitationCodesRepository;
import io.tech1.framework.b2b.postgres.security.jwt.domain.db.PostgresDbInvitationCode;
import io.tech1.framework.domain.base.Username;
import io.tech1.framework.domain.tuples.TuplePresence;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

import static io.tech1.framework.b2b.base.security.jwt.comparators.SecurityJwtSorts.INVITATION_CODES_UNUSED;
import static io.tech1.framework.b2b.base.security.jwt.utilities.SpringAuthoritiesUtility.getSimpleGrantedAuthorities;
import static io.tech1.framework.domain.tuples.TuplePresence.present;
import static java.util.Objects.nonNull;

@SuppressWarnings("JpaQlInspection")
public interface PostgresInvitationCodesRepository extends JpaRepository<PostgresDbInvitationCode, String>, InvitationCodesRepository {
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

    default void delete(InvitationCodeId invitationCodeId) {
        var tuplePresence = this.isPresent(invitationCodeId);
        if (tuplePresence.present()) {
            this.deleteById(invitationCodeId.value());
        }
    }

    default InvitationCodeId saveAs(InvitationCode invitationCode) {
        var entity = this.save(new PostgresDbInvitationCode(invitationCode));
        return entity.invitationCodeId();
    }

    default InvitationCodeId saveAs(Username owner, RequestNewInvitationCodeParams requestNewInvitationCodeParams) {
        var invitationCode = new PostgresDbInvitationCode(
                owner,
                getSimpleGrantedAuthorities(requestNewInvitationCodeParams.authorities())
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
