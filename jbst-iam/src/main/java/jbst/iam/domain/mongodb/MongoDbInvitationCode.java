package jbst.iam.domain.mongodb;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jbst.iam.domain.db.InvitationCode;
import jbst.iam.domain.dto.responses.ResponseInvitationCode;
import jbst.iam.domain.identifiers.InvitationCodeId;
import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Transient;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import jbst.foundation.domain.base.Username;

import java.util.List;
import java.util.Set;

import static jbst.foundation.utilities.random.RandomUtility.randomStringLetterOrNumbersOnly;
import static jbst.foundation.utilities.spring.SpringAuthoritiesUtility.getResponseInvitationCodeAuthoritiesAsField;
import static jbst.foundation.utilities.spring.SpringAuthoritiesUtility.getSimpleGrantedAuthorities;

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

    public static List<MongoDbInvitationCode> dummies1() {
        var invitationCode1 = MongoDbInvitationCode.admin("user1");
        var invitationCode2 = MongoDbInvitationCode.admin("user1");
        var invitationCode3 = MongoDbInvitationCode.admin("user2");
        var invitationCode4 = MongoDbInvitationCode.admin("user2");
        var invitationCode5 = MongoDbInvitationCode.admin("user2");
        var invitationCode6 = MongoDbInvitationCode.admin("user3");

        invitationCode4.setInvited(Username.of("superadmin"));

        return List.of(
                invitationCode1,
                invitationCode2,
                invitationCode3,
                invitationCode4,
                invitationCode5,
                invitationCode6
        );
    }

    public static List<MongoDbInvitationCode> dummies2() {
        var invitationCode1 = MongoDbInvitationCode.admin("owner22", "value22");
        var invitationCode2 = MongoDbInvitationCode.admin("owner22", "abc");
        var invitationCode3 = MongoDbInvitationCode.admin("owner22", "value44");
        var invitationCode4 = MongoDbInvitationCode.admin("owner11", "value222");
        var invitationCode5 = MongoDbInvitationCode.admin("owner11", "value111");
        var invitationCode6 = MongoDbInvitationCode.admin("owner33", "value123", "invited1");
        var invitationCode7 = MongoDbInvitationCode.admin("owner34", "value234", "invited2");
        return List.of(
                invitationCode1,
                invitationCode2,
                invitationCode3,
                invitationCode4,
                invitationCode5,
                invitationCode6,
                invitationCode7
        );
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
