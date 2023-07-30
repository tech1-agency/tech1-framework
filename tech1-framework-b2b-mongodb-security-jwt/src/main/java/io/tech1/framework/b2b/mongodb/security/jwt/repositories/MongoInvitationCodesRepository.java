package io.tech1.framework.b2b.mongodb.security.jwt.repositories;

import io.tech1.framework.b2b.base.security.jwt.domain.db.AnyDbInvitationCode;
import io.tech1.framework.b2b.base.security.jwt.domain.dto.requests.RequestNewInvitationCodeParams;
import io.tech1.framework.b2b.base.security.jwt.domain.dto.responses.ResponseInvitationCode;
import io.tech1.framework.b2b.base.security.jwt.domain.identifiers.InvitationCodeId;
import io.tech1.framework.b2b.base.security.jwt.repositories.AnyDbInvitationCodesRepository;
import io.tech1.framework.b2b.mongodb.security.jwt.domain.db.MongoDbInvitationCode;
import io.tech1.framework.domain.base.Username;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.stream.Collectors;

import static io.tech1.framework.domain.asserts.Asserts.assertNonNullOrThrow;
import static io.tech1.framework.domain.utilities.exceptions.ExceptionsMessagesUtility.entityNotFound;
import static java.util.Objects.nonNull;

@Repository
public interface MongoInvitationCodesRepository extends MongoRepository<MongoDbInvitationCode, String>, AnyDbInvitationCodesRepository {
    // ================================================================================================================
    // Any
    // ================================================================================================================
    default AnyDbInvitationCode requirePresence(InvitationCodeId invitationCodeId) {
        var invitationCode = this.getById(invitationCodeId);
        assertNonNullOrThrow(invitationCode, entityNotFound("DbInvitationCode", invitationCodeId.value()));
        return invitationCode.anyDbInvitationCode();
    }

    default List<ResponseInvitationCode> findResponseCodesByOwner(Username owner) {
        return this.findByOwner(owner).stream()
                .map(MongoDbInvitationCode::getResponseInvitationCode)
                .collect(Collectors.toList());
    }

    default AnyDbInvitationCode findByValueAsAny(String value) {
        var invitationCode = this.findByValue(value);
        return nonNull(invitationCode) ? invitationCode.anyDbInvitationCode() : null;
    }

    default List<ResponseInvitationCode> findUnused() {
        return this.findByInvitedIsNull().stream()
                .map(MongoDbInvitationCode::getResponseInvitationCode)
                .collect(Collectors.toList());
    }

    long countByOwner(Username username);

    default void delete(InvitationCodeId invitationCodeId) {
        this.deleteById(invitationCodeId.value());
    }

    default void saveAs(AnyDbInvitationCode invitationCode) {
        this.save(new MongoDbInvitationCode(invitationCode));
    }

    default void saveAs(Username owner, RequestNewInvitationCodeParams requestNewInvitationCodeParams) {
        var invitationCode = new MongoDbInvitationCode(
                owner,
                requestNewInvitationCodeParams.authorities().stream().map(SimpleGrantedAuthority::new).collect(Collectors.toList())
        );
        this.save(invitationCode);
    }

    // ================================================================================================================
    // Spring Data
    // ================================================================================================================
    List<MongoDbInvitationCode> findByOwner(Username username);
    List<MongoDbInvitationCode> findByInvitedIsNull();
    List<MongoDbInvitationCode> findByInvitedIsNotNull();
    MongoDbInvitationCode findByValue(String value);

    default MongoDbInvitationCode getById(InvitationCodeId invitationCodeId) {
        return this.findById(invitationCodeId.value()).orElse(null);
    }

    void deleteByInvitedIsNull();
    void deleteByInvitedIsNotNull();
}
