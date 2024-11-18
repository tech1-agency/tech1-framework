package jbst.iam.repositories.mongodb;

import jbst.iam.domain.db.InvitationCode;
import jbst.iam.domain.dto.requests.RequestUserRegistration1;
import jbst.iam.domain.identifiers.UserId;
import jbst.iam.domain.jwt.JwtUser;
import jbst.iam.repositories.UsersRepository;
import jbst.iam.domain.mongodb.MongoDbUser;
import tech1.framework.foundation.domain.base.Email;
import tech1.framework.foundation.domain.base.Password;
import tech1.framework.foundation.domain.base.Username;
import tech1.framework.foundation.domain.tuples.TuplePresence;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import static jbst.iam.constants.SecurityJwtConstants.SUPERADMIN;
import static tech1.framework.foundation.domain.tuples.TuplePresence.present;
import static tech1.framework.foundation.utilities.exceptions.ExceptionsMessagesUtility.entityNotFound;
import static java.util.Objects.nonNull;

public interface MongoUsersRepository extends MongoRepository<MongoDbUser, String>, UsersRepository {
    // ================================================================================================================
    // Any
    // ================================================================================================================
    default TuplePresence<JwtUser> isPresent(UserId userId) {
        return this.findById(userId.value())
                .map(entity -> present(entity.asJwtUser()))
                .orElseGet(TuplePresence::absent);
    }

    default JwtUser loadUserByUsername(Username username) throws UsernameNotFoundException {
        var user = this.findByUsername(username);
        if (nonNull(user)) {
            return user.asJwtUser();
        } else {
            throw new UsernameNotFoundException(entityNotFound("Username", username.value()));
        }
    }

    default JwtUser findByUsernameAsJwtUserOrNull(Username username) {
        var user = this.findByUsername(username);
        return nonNull(user) ? user.asJwtUser() : null;
    }

    default JwtUser findByEmailAsJwtUserOrNull(Email email) {
        var user = this.findByEmail(email);
        return nonNull(user) ? user.asJwtUser() : null;
    }

    default UserId saveAs(JwtUser user) {
        var entity = this.save(new MongoDbUser(user));
        return entity.userId();
    }

    default UserId saveAs(RequestUserRegistration1 requestUserRegistration1, Password password, InvitationCode invitationCode) {
        var user = new MongoDbUser(
                requestUserRegistration1.username(),
                password,
                requestUserRegistration1.zoneId(),
                invitationCode.authorities(),
                false
        );
        var entity = this.save(user);
        return entity.userId();
    }

    // ================================================================================================================
    // Spring Data
    // ================================================================================================================
    MongoDbUser findByEmail(Email email);
    MongoDbUser findByUsername(Username username);
    List<MongoDbUser> findByUsernameIn(Set<Username> usernames);
    List<MongoDbUser> findByUsernameIn(List<Username> usernames);

    // ================================================================================================================
    // Queries
    // ================================================================================================================
    @Query(value = "{ 'authorities': ?0}")
    List<MongoDbUser> findByAuthority(SimpleGrantedAuthority authority);

    @Query(value = "{ 'authorities': ?0}", fields = "{ 'id': 0, 'username' : 1}")
    List<MongoDbUser> findByAuthorityProjectionUsernames(SimpleGrantedAuthority authority);

    @Query(value = "{ 'authorities': { '$ne': ?0}}")
    List<MongoDbUser> findByAuthorityNotEqual(SimpleGrantedAuthority authority);

    @Query(value = "{ 'authorities': { '$ne': ?0}}", fields = "{ 'id': 0, 'username' : 1}")
    List<MongoDbUser> findByAuthorityNotEqualProjectionUsernames(SimpleGrantedAuthority authority);

    @Query(value = "{ 'authorities': ?0}", delete = true)
    void deleteByAuthority(SimpleGrantedAuthority authority);

    @Query(value = "{ 'authorities': { '$ne': ?0}}", delete = true)
    void deleteByAuthorityNotEqual(SimpleGrantedAuthority authority);

    default List<MongoDbUser> findByAuthoritySuperadmin() {
        return this.findByAuthority(SUPERADMIN);
    }

    default Set<Username> findSuperadminsUsernames() {
        return this.findByAuthorityProjectionUsernames(SUPERADMIN).stream().map(MongoDbUser::getUsername).collect(Collectors.toSet());
    }

    default List<MongoDbUser> findByAuthorityNotSuperadmin() {
        return this.findByAuthorityNotEqual(SUPERADMIN);
    }

    default Set<Username> findNotSuperadminsUsernames() {
        return this.findByAuthorityNotEqualProjectionUsernames(SUPERADMIN).stream().map(MongoDbUser::getUsername).collect(Collectors.toSet());
    }

    default void deleteByAuthoritySuperadmin() {
        this.deleteByAuthority(SUPERADMIN);
    }

    default void deleteByAuthorityNotSuperadmin() {
        this.deleteByAuthorityNotEqual(SUPERADMIN);
    }
}
