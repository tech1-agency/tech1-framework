package io.tech1.framework.b2b.mongodb.security.jwt.repositories;

import io.tech1.framework.b2b.base.security.jwt.domain.identifiers.InvitationCodeId;
import io.tech1.framework.b2b.base.security.jwt.repositories.AnyDbInvitationCodesRepository;
import io.tech1.framework.b2b.mongodb.security.jwt.domain.db.MongoDbInvitationCode;
import io.tech1.framework.domain.base.Username;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

import static io.tech1.framework.domain.asserts.Asserts.assertNonNullOrThrow;
import static io.tech1.framework.domain.utilities.exceptions.ExceptionsMessagesUtility.entityNotFound;

@Repository
public interface MongoInvitationCodesRepository extends MongoRepository<MongoDbInvitationCode, String>, AnyDbInvitationCodesRepository {
    // ================================================================================================================
    // Spring Data
    // ================================================================================================================
    List<MongoDbInvitationCode> findByOwner(Username username);
    List<MongoDbInvitationCode> findByInvitedIsNull();
    MongoDbInvitationCode findByValue(String value);
    long countByOwner(Username username);

    default MongoDbInvitationCode getById(InvitationCodeId invitationCodeId) {
        return this.findById(invitationCodeId.value()).orElse(null);
    }

    default MongoDbInvitationCode requirePresence(InvitationCodeId invitationCodeId) {
        var invitationCode = this.getById(invitationCodeId);
        assertNonNullOrThrow(invitationCode, entityNotFound("DbInvitationCode", invitationCodeId.value()));
        return invitationCode;
    }

    // ================================================================================================================
    // Queries
    // ================================================================================================================
    @Deprecated(since = "v1.14, add spring-data methods")
    @Query(value = "{ 'invited': { '$exists': true}}")
    List<MongoDbInvitationCode> findByInvitedAlreadyUsed();

    @Deprecated(since = "v1.14, add spring-data methods")
    @Query(value = "{ 'invited': { '$exists': false}}")
    List<MongoDbInvitationCode> findByInvitedNotUsed();

    @Deprecated(since = "v1.14, add spring-data methods")
    @Query(value = "{ 'invited': { '$exists': true}}", delete = true)
    void deleteByInvitedAlreadyUsed();

    @Deprecated(since = "v1.14, add spring-data methods")
    @Query(value = "{ 'invited': { '$exists': false}}", delete = true)
    void deleteByInvitedNotUsed();
}
