package jbst.iam.repositories.postgres;

import jbst.foundation.utilities.time.TimestampUtility;
import jbst.iam.domain.db.UserToken;
import jbst.iam.domain.dto.requests.RequestUserToken;
import jbst.iam.domain.identifiers.TokenId;
import jbst.iam.domain.postgres.db.PostgresDbUserToken;
import jbst.iam.repositories.UsersTokensRepository;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.transaction.annotation.Transactional;

import static java.util.Objects.nonNull;

public interface PostgresUsersTokensRepository extends JpaRepository<PostgresDbUserToken, String>, UsersTokensRepository {

    // ================================================================================================================
    // Any
    // ================================================================================================================
    default UserToken findByValueAsAny(String value) {
        var entity = this.findByValue(value);
        return nonNull(entity) ? entity.asUserToken() : null;
    }

    @Transactional
    @Modifying
    default void cleanupExpired() {
        this.deleteAllByExpiryTimestampBefore(TimestampUtility.getCurrentTimestamp());
    }

    @Transactional
    @Modifying
    default void cleanupUsed() {
        this.deleteAllByUsedIsTrue();
    }

    default TokenId saveAs(UserToken token) {
        var entity = this.save(new PostgresDbUserToken(token));
        return entity.tokenId();
    }

    default UserToken saveAs(RequestUserToken request) {
        var entity = this.save(
                new PostgresDbUserToken(
                        request
                )
        );
        return entity.asUserToken();
    }

    // ================================================================================================================
    // Spring Data
    // ================================================================================================================
    PostgresDbUserToken findByValue(String value);
    void deleteAllByExpiryTimestampBefore(long timestamp);
    void deleteAllByUsedIsTrue();
}
