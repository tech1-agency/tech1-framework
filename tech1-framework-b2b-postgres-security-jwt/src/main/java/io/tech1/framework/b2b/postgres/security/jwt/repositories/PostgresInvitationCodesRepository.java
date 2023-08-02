package io.tech1.framework.b2b.postgres.security.jwt.repositories;

import io.tech1.framework.b2b.base.security.jwt.domain.db.AnyDbInvitationCode;
import io.tech1.framework.b2b.base.security.jwt.domain.dto.requests.RequestNewInvitationCodeParams;
import io.tech1.framework.b2b.base.security.jwt.domain.dto.responses.ResponseInvitationCode;
import io.tech1.framework.b2b.base.security.jwt.domain.identifiers.InvitationCodeId;
import io.tech1.framework.b2b.base.security.jwt.repositories.AnyDbInvitationCodesRepository;
import io.tech1.framework.b2b.postgres.security.jwt.domain.db.PostgresDbInvitationCode;
import io.tech1.framework.domain.base.Username;
import io.tech1.framework.domain.tuples.TuplePresence;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

import static java.util.Objects.nonNull;

@SuppressWarnings("JpaQlInspection")
public interface PostgresInvitationCodesRepository extends JpaRepository<PostgresDbInvitationCode, String>, AnyDbInvitationCodesRepository {
    // ================================================================================================================
    // Any
    // ================================================================================================================
    default TuplePresence<AnyDbInvitationCode> isPresent(InvitationCodeId invitationCodeId) {
        return this.findById(invitationCodeId.value())
                .map(mongoDbInvitationCode -> TuplePresence.present(mongoDbInvitationCode.anyDbInvitationCode()))
                .orElseGet(TuplePresence::absent);
    }

    default List<ResponseInvitationCode> findResponseCodesByOwner(Username owner) {
        return this.findByOwner(owner).stream()
                .map(PostgresDbInvitationCode::getResponseInvitationCode)
                .collect(Collectors.toList());
    }

    default AnyDbInvitationCode findByValueAsAny(String value) {
        var invitationCode = this.findByValue(value);
        return nonNull(invitationCode) ? invitationCode.anyDbInvitationCode() : null;
    }

    default List<ResponseInvitationCode> findUnused() {
        return this.findByInvitedIsNull(this.getUnusedSort()).stream()
                .map(PostgresDbInvitationCode::getResponseInvitationCode)
                .collect(Collectors.toList());
    }

    long countByOwner(Username username);

    default void delete(InvitationCodeId invitationCodeId) {
        var tuplePresence = this.isPresent(invitationCodeId);
        if (tuplePresence.present()) {
            this.deleteById(invitationCodeId.value());
        }
    }

    default InvitationCodeId saveAs(AnyDbInvitationCode invitationCode) {
        var entity = this.save(new PostgresDbInvitationCode(invitationCode));
        return entity.invitationCodeId();
    }

    default InvitationCodeId saveAs(Username owner, RequestNewInvitationCodeParams requestNewInvitationCodeParams) {
        var invitationCode = new PostgresDbInvitationCode(
                owner,
                requestNewInvitationCodeParams.authorities().stream().map(SimpleGrantedAuthority::new).collect(Collectors.toList())
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
