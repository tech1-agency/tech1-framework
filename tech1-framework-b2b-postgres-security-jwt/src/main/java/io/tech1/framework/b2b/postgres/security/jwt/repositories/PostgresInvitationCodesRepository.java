package io.tech1.framework.b2b.postgres.security.jwt.repositories;

import io.tech1.framework.b2b.base.security.jwt.domain.db.AnyDbInvitationCode;
import io.tech1.framework.b2b.base.security.jwt.domain.identifiers.InvitationCodeId;
import io.tech1.framework.b2b.base.security.jwt.repositories.AnyDbInvitationCodesRepository;
import io.tech1.framework.b2b.postgres.security.jwt.domain.db.PostgresDbInvitationCode;
import io.tech1.framework.domain.base.Username;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static io.tech1.framework.domain.asserts.Asserts.assertNonNullOrThrow;
import static io.tech1.framework.domain.utilities.exceptions.ExceptionsMessagesUtility.entityNotFound;
import static java.util.Objects.nonNull;

@SuppressWarnings("JpaQlInspection")
public interface PostgresInvitationCodesRepository extends JpaRepository<PostgresDbInvitationCode, String>, AnyDbInvitationCodesRepository {
    // ================================================================================================================
    // Any
    // ================================================================================================================
    default AnyDbInvitationCode requirePresence(InvitationCodeId invitationCodeId) {
        var invitationCode = this.getReferenceById(invitationCodeId.value());
        assertNonNullOrThrow(invitationCode, entityNotFound("DbInvitationCode", invitationCodeId.value()));
        return invitationCode.anyDbInvitationCode();
    }

    long countByOwner(Username username);

    default AnyDbInvitationCode findByValueAsAny(String value) {
        var invitationCode = this.findByValue(value);
        return nonNull(invitationCode) ? invitationCode.anyDbInvitationCode() : null;
    }

    // ================================================================================================================
    // Spring Data
    // ================================================================================================================
    List<PostgresDbInvitationCode> findByOwner(Username username);
    List<PostgresDbInvitationCode> findByInvitedIsNull();
    List<PostgresDbInvitationCode> findByInvitedIsNotNull();
    PostgresDbInvitationCode findByValue(String value);

    @Transactional
    @Modifying
    void deleteByInvitedIsNull();

    @Transactional
    @Modifying
    void deleteByInvitedIsNotNull();
}
