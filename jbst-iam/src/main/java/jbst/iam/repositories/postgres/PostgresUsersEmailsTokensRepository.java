package jbst.iam.repositories.postgres;

import jbst.foundation.utilities.time.TimestampUtility;
import jbst.iam.domain.db.UserEmailToken;
import jbst.iam.domain.identifiers.TokenId;
import jbst.iam.domain.postgres.db.PostgresDbUserEmailToken;
import jbst.iam.repositories.UsersEmailsTokensRepository;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.transaction.annotation.Transactional;

public interface PostgresUsersEmailsTokensRepository extends JpaRepository<PostgresDbUserEmailToken, String>, UsersEmailsTokensRepository {

    // ================================================================================================================
    // Any
    // ================================================================================================================
    default UserEmailToken findByValueAsAny(String value) {
        return this.findByValue(value).asUserEmailToken();
    }

    @Transactional
    @Modifying
    default void cleanupExpired() {
        this.deleteAllByExpiryTimestampBefore(TimestampUtility.getCurrentTimestamp());
    }

    default TokenId saveAs(UserEmailToken token) {
        var entity = this.save(new PostgresDbUserEmailToken(token));
        return entity.tokenId();
    }

    // ================================================================================================================
    // Spring Data
    // ================================================================================================================
    PostgresDbUserEmailToken findByValue(String value);
    void deleteAllByExpiryTimestampBefore(long timestamp);
}
