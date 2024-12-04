package jbst.iam.repositories.mongodb;

import jbst.foundation.utilities.time.TimestampUtility;
import jbst.iam.domain.db.UserEmailToken;
import jbst.iam.domain.dto.requests.RequestUserEmailToken;
import jbst.iam.domain.identifiers.TokenId;
import jbst.iam.domain.mongodb.MongoDbUserEmailToken;
import jbst.iam.repositories.UsersEmailsTokensRepository;
import org.springframework.data.mongodb.repository.MongoRepository;

import static java.util.Objects.nonNull;

public interface MongoUsersEmailsTokensRepository extends MongoRepository<MongoDbUserEmailToken, String>, UsersEmailsTokensRepository {

    // ================================================================================================================
    // Any
    // ================================================================================================================
    default UserEmailToken findByValueAsAny(String value) {
        var entity = this.findByValue(value);
        return nonNull(entity) ? entity.asUserEmailToken() : null;
    }

    default void cleanupExpired() {
        this.deleteAllByExpiryTimestampBefore(TimestampUtility.getCurrentTimestamp());
    }

    default TokenId saveAs(UserEmailToken token) {
        var entity = this.save(new MongoDbUserEmailToken(token));
        return entity.tokenId();
    }

    default UserEmailToken saveAs(RequestUserEmailToken request) {
        var entity = this.save(
                new MongoDbUserEmailToken(
                        request
                )
        );
        return entity.asUserEmailToken();
    }

    // ================================================================================================================
    // Spring Data
    // ================================================================================================================
    MongoDbUserEmailToken findByValue(String value);
    void deleteAllByExpiryTimestampBefore(long timestamp);
}
