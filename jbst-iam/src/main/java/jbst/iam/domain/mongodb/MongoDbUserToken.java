package jbst.iam.domain.mongodb;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jbst.foundation.domain.base.Email;
import jbst.foundation.domain.constants.JbstConstants;
import jbst.foundation.domain.time.TimeAmount;
import jbst.iam.domain.db.UserToken;
import jbst.iam.domain.dto.requests.RequestUserToken;
import jbst.iam.domain.enums.UserTokenType;
import jbst.iam.domain.identifiers.TokenId;
import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Transient;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.temporal.ChronoUnit;
import java.util.List;

import static jbst.foundation.utilities.random.RandomUtility.*;
import static jbst.foundation.utilities.time.TimestampUtility.getFutureRange;
import static jbst.foundation.utilities.time.TimestampUtility.getPastRange;

// Lombok
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@Setter
@EqualsAndHashCode
@ToString
// Mongodb
@Document(collection = MongoDbUserToken.MONGO_TABLE_NAME)
public class MongoDbUserToken {
    public static final String MONGO_TABLE_NAME = "jbst_users_emails_tokens";

    @Id
    private String id;
    private Email email;
    private String value;
    private UserTokenType type;
    private long expiryTimestamp;

    public MongoDbUserToken(
            Email email,
            String value,
            UserTokenType type,
            long expiryTimestamp
    ) {
        this.email = email;
        this.value = value;
        this.type = type;
        this.expiryTimestamp = expiryTimestamp;
    }

    public MongoDbUserToken(RequestUserToken request) {
        this(
                request.email(),
                randomStringLetterOrNumbersOnly(36),
                request.type(),
                getFutureRange(new TimeAmount(24, ChronoUnit.HOURS)).to()
        );
    }

    public MongoDbUserToken(UserToken token) {
        this.id = token.id().value();
        this.email = token.email();
        this.value = token.value();
        this.type = token.type();
        this.expiryTimestamp = token.expiryTimestamp();
    }

    public static MongoDbUserToken random(
            Email email,
            long expiryTimestamp
    ) {
        return new MongoDbUserToken(
                email,
                randomString(),
                randomEnum(UserTokenType.class),
                expiryTimestamp
        );
    }

    public static List<MongoDbUserToken> dummies1() {
        var token1 = MongoDbUserToken.random(
                Email.of("token1@" + JbstConstants.Domains.HARDCODED),
                getFutureRange(new TimeAmount(1, ChronoUnit.DAYS)).to()
        );
        var token2 = MongoDbUserToken.random(
                Email.of("token2@" + JbstConstants.Domains.HARDCODED),
                getFutureRange(new TimeAmount(1, ChronoUnit.DAYS)).to()
        );
        var token3 = MongoDbUserToken.random(
                Email.of("token3@" + JbstConstants.Domains.HARDCODED),
                getPastRange(new TimeAmount(1, ChronoUnit.DAYS)).from()
        );
        var token4 = MongoDbUserToken.random(
                Email.of("token4@" + JbstConstants.Domains.HARDCODED),
                getPastRange(new TimeAmount(1, ChronoUnit.DAYS)).from()
        );
        return List.of(
                token1,
                token2,
                token3,
                token4
        );
    }

    @JsonIgnore
    @Transient
    public TokenId tokenId() {
        return new TokenId(this.id);
    }

    @JsonIgnore
    @Transient
    public UserToken asUserToken() {
        return new UserToken(
                new TokenId(this.id),
                this.email,
                this.value,
                this.type,
                this.expiryTimestamp
        );
    }
}
