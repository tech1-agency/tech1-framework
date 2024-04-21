package io.tech1.framework.b2b.mongodb.security.jwt.domain.db;

import com.fasterxml.jackson.annotation.JsonIgnore;
import io.tech1.framework.b2b.base.security.jwt.domain.db.InvitationCode;
import io.tech1.framework.b2b.base.security.jwt.domain.dto.responses.ResponseInvitationCode;
import io.tech1.framework.b2b.base.security.jwt.domain.identifiers.InvitationCodeId;
import io.tech1.framework.domain.base.Username;
import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Transient;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.Set;

import static io.tech1.framework.b2b.base.security.jwt.constants.SecurityJwtConstants.DEFAULT_INVITATION_CODE_LENGTH;
import static io.tech1.framework.b2b.base.security.jwt.utilities.SpringAuthoritiesUtility.getResponseInvitationCodeAuthoritiesAsField;
import static io.tech1.framework.domain.utilities.random.RandomUtility.randomStringLetterOrNumbersOnly;

// Lombok
@NoArgsConstructor
@Getter
@Setter
@EqualsAndHashCode
@ToString
// Mongodb
@Document(collection = MongoDbInvitationCode.MONGO_TABLE_NAME)
public class MongoDbInvitationCode {
    public static final String MONGO_TABLE_NAME = "tech1_invitation_codes";

    @Id
    private String id;
    private Username owner;
    private Set<SimpleGrantedAuthority> authorities;
    private String value;
    private Username invited;

    public MongoDbInvitationCode(Username owner, Set<SimpleGrantedAuthority> authorities) {
        this.owner = owner;
        this.authorities = authorities;
        this.value = randomStringLetterOrNumbersOnly(DEFAULT_INVITATION_CODE_LENGTH);
    }

    public MongoDbInvitationCode(InvitationCode invitationCode) {
        this.id = invitationCode.id().value();
        this.owner = invitationCode.owner();
        this.authorities = invitationCode.authorities();
        this.value = invitationCode.value();
        this.invited = invitationCode.invited();
    }

    @JsonIgnore
    @Transient
    public InvitationCodeId invitationCodeId() {
        return new InvitationCodeId(this.id);
    }

    @JsonIgnore
    @Transient
    public InvitationCode invitationCode() {
        return new InvitationCode(
                this.invitationCodeId(),
                this.owner,
                this.authorities,
                this.value,
                this.invited
        );
    }

    @JsonIgnore
    @Transient
    public ResponseInvitationCode responseInvitationCode() {
        return new ResponseInvitationCode(
                this.invitationCodeId(),
                this.owner,
                getResponseInvitationCodeAuthoritiesAsField(this.authorities),
                this.value,
                this.invited
        );
    }
}
