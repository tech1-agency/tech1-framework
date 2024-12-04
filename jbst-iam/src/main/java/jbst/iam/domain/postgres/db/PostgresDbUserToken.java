package jbst.iam.domain.postgres.db;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jbst.foundation.domain.base.Email;
import jbst.foundation.domain.constants.JbstConstants;
import jbst.foundation.domain.converters.columns.PostgresEmailConverter;
import jbst.foundation.domain.time.TimeAmount;
import jbst.iam.domain.db.UserToken;
import jbst.iam.domain.dto.requests.RequestUserToken;
import jbst.iam.domain.enums.UserTokenType;
import jbst.iam.domain.identifiers.TokenId;
import jbst.iam.domain.postgres.superclasses.PostgresDbAbstractPersistable0;
import lombok.*;
import org.jetbrains.annotations.NotNull;
import org.springframework.data.annotation.Transient;

import java.time.temporal.ChronoUnit;
import java.util.List;

import static jbst.foundation.utilities.random.RandomUtility.randomEnum;
import static jbst.foundation.utilities.random.RandomUtility.randomStringLetterOrNumbersOnly;
import static jbst.foundation.utilities.time.TimestampUtility.getFutureRange;
import static jbst.foundation.utilities.time.TimestampUtility.getPastRange;

@SuppressWarnings("JpaDataSourceORMInspection")
// Lombok
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@Setter
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
// JPA
@Entity
@Table(name = PostgresDbUserToken.PG_TABLE_NAME)
public class PostgresDbUserToken extends PostgresDbAbstractPersistable0 {
    public static final String PG_TABLE_NAME = "jbst_users_emails_tokens";

    @Convert(converter = PostgresEmailConverter.class)
    @Column(nullable = false, updatable = false)
    private Email email;

    @Column(length = 36, nullable = false, updatable = false)
    private String value;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, updatable = false)
    private UserTokenType type;

    @Column(name = "expiry_timestamp", nullable = false, updatable = false)
    private long expiryTimestamp;

    public PostgresDbUserToken(
            @NotNull Email email,
            @NotNull String value,
            @NotNull UserTokenType type,
            long expiryTimestamp
    ) {
        this.email = email;
        this.value = value;
        this.type = type;
        this.expiryTimestamp = expiryTimestamp;
    }

    public PostgresDbUserToken(RequestUserToken request) {
        this(
                request.email(),
                randomStringLetterOrNumbersOnly(36),
                request.type(),
                getFutureRange(new TimeAmount(24, ChronoUnit.HOURS)).to()
        );
    }

    public PostgresDbUserToken(UserToken token) {
        this.id = token.id().value();
        this.email = token.email();
        this.value = token.value();
        this.type = token.type();
        this.expiryTimestamp = token.expiryTimestamp();
    }

    public static PostgresDbUserToken random(
            Email email,
            long expiryTimestamp
    ) {
        return new PostgresDbUserToken(
                email,
                randomStringLetterOrNumbersOnly(36),
                randomEnum(UserTokenType.class),
                expiryTimestamp
        );
    }

    public static List<PostgresDbUserToken> dummies1() {
        var token1 = PostgresDbUserToken.random(
                Email.of("token1@" + JbstConstants.Domains.HARDCODED),
                getFutureRange(new TimeAmount(1, ChronoUnit.DAYS)).to()
        );
        var token2 = PostgresDbUserToken.random(
                Email.of("token2@" + JbstConstants.Domains.HARDCODED),
                getFutureRange(new TimeAmount(1, ChronoUnit.DAYS)).to()
        );
        var token3 = PostgresDbUserToken.random(
                Email.of("token3@" + JbstConstants.Domains.HARDCODED),
                getPastRange(new TimeAmount(1, ChronoUnit.DAYS)).from()
        );
        var token4 = PostgresDbUserToken.random(
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
