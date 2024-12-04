package jbst.iam.repositories.mongodb;

import jbst.foundation.utilities.time.TimestampUtility;
import jbst.iam.domain.db.UserToken;
import jbst.iam.domain.dto.requests.RequestUserToken;
import jbst.iam.domain.identifiers.TokenId;
import jbst.iam.domain.mongodb.MongoDbUserToken;
import jbst.iam.repositories.UsersTokensRepository;
import org.springframework.data.mongodb.repository.MongoRepository;

import static java.util.Objects.nonNull;

public interface MongoUsersTokensRepository extends MongoRepository<MongoDbUserToken, String>, UsersTokensRepository {

    // ================================================================================================================
    // Any
    // ================================================================================================================
    default UserToken findByValueAsAny(String value) {
        var entity = this.findByValue(value);
        return nonNull(entity) ? entity.asUserToken() : null;
    }

    default void cleanupExpired() {
        this.deleteAllByExpiryTimestampBefore(TimestampUtility.getCurrentTimestamp());
    }

    default void cleanupUsed() {
        this.deleteAllByUsedIsTrue();
    }

    default TokenId saveAs(UserToken token) {
        var entity = this.save(new MongoDbUserToken(token));
        return entity.tokenId();
    }

    default UserToken saveAs(RequestUserToken request) {
        var entity = this.save(
                new MongoDbUserToken(
                        request
                )
        );
        return entity.asUserToken();
    }

    // ================================================================================================================
    // Spring Data
    // ================================================================================================================
    MongoDbUserToken findByValue(String value);
    void deleteAllByExpiryTimestampBefore(long timestamp);
    void deleteAllByUsedIsTrue();
}
