package jbst.iam.repositories.postgres;

import jbst.foundation.utilities.time.TimestampUtility;
import jbst.iam.domain.db.UserEmailToken;
import jbst.iam.domain.dto.requests.RequestUserEmailToken;
import jbst.iam.domain.identifiers.TokenId;
import jbst.iam.domain.postgres.db.PostgresDbUserEmailToken;
import jbst.iam.repositories.UsersEmailsTokensRepository;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.transaction.annotation.Transactional;

import static java.util.Objects.nonNull;

public interface PostgresUsersEmailsTokensRepository extends JpaRepository<PostgresDbUserEmailToken, String>, UsersEmailsTokensRepository {

    // ================================================================================================================
    // Any
    // ================================================================================================================
    default UserEmailToken findByValueAsAny(String value) {
        var entity = this.findByValue(value);
        return nonNull(entity) ? entity.asUserEmailToken() : null;
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

    default UserEmailToken saveAs(RequestUserEmailToken request) {
        var entity = this.save(
                new PostgresDbUserEmailToken(
                        request
                )
        );
        return entity.asUserEmailToken();
    }

    // ================================================================================================================
    // Spring Data
    // ================================================================================================================
    PostgresDbUserEmailToken findByValue(String value);
    void deleteAllByExpiryTimestampBefore(long timestamp);
}
