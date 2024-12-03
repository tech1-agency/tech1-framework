package jbst.iam.domain.mongodb;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jbst.foundation.domain.base.Email;
import jbst.iam.domain.db.UserEmailToken;
import jbst.iam.domain.enums.UserEmailTokenType;
import jbst.iam.domain.identifiers.TokenId;
import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Transient;
import org.springframework.data.mongodb.core.mapping.Document;

// Lombok
@NoArgsConstructor
@Getter
@Setter
@EqualsAndHashCode
@ToString
// Mongodb
@Document(collection = MongoDbUserEmailToken.MONGO_TABLE_NAME)
public class MongoDbUserEmailToken {
    public static final String MONGO_TABLE_NAME = "jbst_user_email_tokens";

    @Id
    private String id;
    private Email email;
    private String value;
    private UserEmailTokenType type;
    private long expiryTimestamp;

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
