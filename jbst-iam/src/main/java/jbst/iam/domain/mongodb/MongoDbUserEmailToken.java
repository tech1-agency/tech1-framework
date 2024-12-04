package jbst.iam.domain.mongodb;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jbst.foundation.domain.base.Email;
import jbst.foundation.domain.constants.JbstConstants;
import jbst.foundation.domain.time.TimeAmount;
import jbst.iam.domain.db.UserEmailToken;
import jbst.iam.domain.dto.requests.RequestUserEmailToken;
import jbst.iam.domain.enums.UserEmailTokenType;
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
@Document(collection = MongoDbUserEmailToken.MONGO_TABLE_NAME)
public class MongoDbUserEmailToken {
    public static final String MONGO_TABLE_NAME = "jbst_users_emails_tokens";

    @Id
    private String id;
    private Email email;
    private String value;
    private UserEmailTokenType type;
    private long expiryTimestamp;

    public MongoDbUserEmailToken(
            Email email,
            String value,
            UserEmailTokenType type,
            long expiryTimestamp
    ) {
        this.email = email;
        this.value = value;
        this.type = type;
        this.expiryTimestamp = expiryTimestamp;
    }

    public MongoDbUserEmailToken(RequestUserEmailToken request) {
        this(
                request.email(),
                randomStringLetterOrNumbersOnly(36),
                request.type(),
                getFutureRange(new TimeAmount(24, ChronoUnit.HOURS)).to()
        );
    }

    public MongoDbUserEmailToken(UserEmailToken token) {
        this.id = token.id().value();
        this.email = token.email();
        this.value = token.value();
        this.type = token.type();
        this.expiryTimestamp = token.expiryTimestamp();
    }

    public static MongoDbUserEmailToken random(
            Email email,
            long expiryTimestamp
    ) {
        return new MongoDbUserEmailToken(
                email,
                randomString(),
                randomEnum(UserEmailTokenType.class),
                expiryTimestamp
        );
    }

    public static List<MongoDbUserEmailToken> dummies1() {
        var token1 = MongoDbUserEmailToken.random(
                Email.of("token1@" + JbstConstants.Domains.HARDCODED),
                getFutureRange(new TimeAmount(1, ChronoUnit.DAYS)).to()
        );
        var token2 = MongoDbUserEmailToken.random(
                Email.of("token2@" + JbstConstants.Domains.HARDCODED),
                getFutureRange(new TimeAmount(1, ChronoUnit.DAYS)).to()
        );
        var token3 = MongoDbUserEmailToken.random(
                Email.of("token3@" + JbstConstants.Domains.HARDCODED),
                getPastRange(new TimeAmount(1, ChronoUnit.DAYS)).from()
        );
        var token4 = MongoDbUserEmailToken.random(
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
    public UserEmailToken asUserEmailToken() {
        return new UserEmailToken(
                new TokenId(this.id),
                this.email,
                this.value,
                this.type,
                this.expiryTimestamp
        );
    }
}
