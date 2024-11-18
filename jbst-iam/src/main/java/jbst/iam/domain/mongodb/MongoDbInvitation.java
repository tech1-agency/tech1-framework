package jbst.iam.domain.mongodb;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jbst.iam.domain.db.Invitation;
import jbst.iam.domain.dto.responses.ResponseInvitation;
import jbst.iam.domain.identifiers.InvitationId;
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
@Document(collection = MongoDbInvitation.MONGO_TABLE_NAME)
public class MongoDbInvitation {
    public static final String MONGO_TABLE_NAME = "jbst_invitations";

    @Id
    private String id;
    private Username owner;
    private Set<SimpleGrantedAuthority> authorities;
    private String value;
    private Username invited;

    public MongoDbInvitation(Username owner, Set<SimpleGrantedAuthority> authorities) {
        this.owner = owner;
        this.authorities = authorities;
        this.value = randomStringLetterOrNumbersOnly(Invitation.DEFAULT_INVITATION_CODE_LENGTH);
    }

    public MongoDbInvitation(Invitation invitation) {
        this.id = invitation.id().value();
        this.owner = invitation.owner();
        this.authorities = invitation.authorities();
        this.value = invitation.value();
        this.invited = invitation.invited();
    }

    public static MongoDbInvitation admin(String owner) {
        return new MongoDbInvitation(Username.of(owner), getSimpleGrantedAuthorities("admin"));
    }

    public static MongoDbInvitation admin(String owner, String value) {
        var invitationCode = admin(owner);
        invitationCode.setValue(value);
        return invitationCode;
    }

    public static MongoDbInvitation admin(String owner, String value, String invited) {
        var invitationCode = admin(owner, value);
        invitationCode.setInvited(Username.of(invited));
        return invitationCode;
    }

    public static List<MongoDbInvitation> dummies1() {
        var invitationCode1 = MongoDbInvitation.admin("user1");
        var invitationCode2 = MongoDbInvitation.admin("user1");
        var invitationCode3 = MongoDbInvitation.admin("user2");
        var invitationCode4 = MongoDbInvitation.admin("user2");
        var invitationCode5 = MongoDbInvitation.admin("user2");
        var invitationCode6 = MongoDbInvitation.admin("user3");

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

    public static List<MongoDbInvitation> dummies2() {
        var invitationCode1 = MongoDbInvitation.admin("owner22", "value22");
        var invitationCode2 = MongoDbInvitation.admin("owner22", "abc");
        var invitationCode3 = MongoDbInvitation.admin("owner22", "value44");
        var invitationCode4 = MongoDbInvitation.admin("owner11", "value222");
        var invitationCode5 = MongoDbInvitation.admin("owner11", "value111");
        var invitationCode6 = MongoDbInvitation.admin("owner33", "value123", "invited1");
        var invitationCode7 = MongoDbInvitation.admin("owner34", "value234", "invited2");
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
    public InvitationId invitationId() {
        return new InvitationId(this.id);
    }

    @JsonIgnore
    @Transient
    public Invitation invitationCode() {
        return new Invitation(
                this.invitationId(),
                this.owner,
                this.authorities,
                this.value,
                this.invited
        );
    }

    @JsonIgnore
    @Transient
    public ResponseInvitation responseInvitationCode() {
        return ResponseInvitation.of(
                this.invitationId(),
                this.owner,
                getResponseInvitationCodeAuthoritiesAsField(this.authorities),
                this.value,
                this.invited
        );
    }
}
