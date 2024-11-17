package tech1.framework.iam.domain.mongodb;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Transient;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import tech1.framework.foundation.domain.base.Username;
import tech1.framework.iam.domain.db.InvitationCode;
import tech1.framework.iam.domain.dto.responses.ResponseInvitationCode;
import tech1.framework.iam.domain.identifiers.InvitationCodeId;

import java.util.Set;

import static tech1.framework.foundation.utilities.random.RandomUtility.randomStringLetterOrNumbersOnly;
import static tech1.framework.iam.utilities.SpringAuthoritiesUtility.getResponseInvitationCodeAuthoritiesAsField;
import static tech1.framework.iam.utilities.SpringAuthoritiesUtility.getSimpleGrantedAuthorities;

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
        this.value = randomStringLetterOrNumbersOnly(InvitationCode.DEFAULT_INVITATION_CODE_LENGTH);
    }

    public MongoDbInvitationCode(InvitationCode invitationCode) {
        this.id = invitationCode.id().value();
        this.owner = invitationCode.owner();
        this.authorities = invitationCode.authorities();
        this.value = invitationCode.value();
        this.invited = invitationCode.invited();
    }

    public static MongoDbInvitationCode admin(String owner) {
        return new MongoDbInvitationCode(Username.of(owner), getSimpleGrantedAuthorities("admin"));
    }

    public static MongoDbInvitationCode admin(String owner, String value) {
        var invitationCode = admin(owner);
        invitationCode.setValue(value);
        return invitationCode;
    }

    public static MongoDbInvitationCode admin(String owner, String value, String invited) {
        var invitationCode = admin(owner, value);
        invitationCode.setInvited(Username.of(invited));
        return invitationCode;
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
        return ResponseInvitationCode.of(
                this.invitationCodeId(),
                this.owner,
                getResponseInvitationCodeAuthoritiesAsField(this.authorities),
                this.value,
                this.invited
        );
    }
}
