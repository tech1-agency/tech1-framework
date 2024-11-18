package jbst.iam.domain.postgres.db;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jbst.iam.converters.columns.PostgresSetOfSimpleGrantedAuthoritiesConverter;
import jbst.iam.domain.db.Invitation;
import jbst.iam.domain.dto.responses.ResponseInvitation;
import jbst.iam.domain.identifiers.InvitationId;
import jbst.iam.domain.postgres.superclasses.PostgresDbAbstractPersistable0;
import lombok.*;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import jbst.foundation.domain.base.Username;
import jbst.foundation.domain.converters.columns.PostgresUsernameConverter;

import java.util.List;
import java.util.Set;

import static jbst.foundation.utilities.random.RandomUtility.randomStringLetterOrNumbersOnly;
import static jbst.foundation.utilities.spring.SpringAuthoritiesUtility.getResponseInvitationCodeAuthoritiesAsField;
import static jbst.foundation.utilities.spring.SpringAuthoritiesUtility.getSimpleGrantedAuthorities;

// Lombok
@NoArgsConstructor
@Getter
@Setter
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
// JPA
@Entity
@Table(name = PostgresDbInvitation.PG_TABLE_NAME)
public class PostgresDbInvitation extends PostgresDbAbstractPersistable0 {
    public static final String PG_TABLE_NAME = "jbst_invitations";

    @Convert(converter = PostgresUsernameConverter.class)
    @Column(nullable = false, updatable = false)
    private Username owner;

    @Convert(converter = PostgresSetOfSimpleGrantedAuthoritiesConverter.class)
    @Column(length = 1024, nullable = false)
    private Set<SimpleGrantedAuthority> authorities;

    @Column(nullable = false)
    private String value;

    @Convert(converter = PostgresUsernameConverter.class)
    @Column
    private Username invited;

    public PostgresDbInvitation(Username owner, Set<SimpleGrantedAuthority> authorities) {
        this.owner = owner;
        this.authorities = authorities;
        this.value = randomStringLetterOrNumbersOnly(Invitation.DEFAULT_INVITATION_CODE_LENGTH);
    }

    public PostgresDbInvitation(Invitation invitation) {
        this.id = invitation.id().value();
        this.owner = invitation.owner();
        this.authorities = invitation.authorities();
        this.value = invitation.value();
        this.invited = invitation.invited();
    }

    public static PostgresDbInvitation admin(String owner) {
        return new PostgresDbInvitation(Username.of(owner), getSimpleGrantedAuthorities("admin"));
    }

    public static PostgresDbInvitation admin(String owner, String value) {
        var invitationCode = admin(owner);
        invitationCode.setValue(value);
        return invitationCode;
    }

    public static PostgresDbInvitation admin(String owner, String value, String invited) {
        var invitationCode = admin(owner, value);
        invitationCode.setInvited(Username.of(invited));
        return invitationCode;
    }

    public static List<PostgresDbInvitation> dummies1() {
        var invitationCode1 = PostgresDbInvitation.admin("user1");
        var invitationCode2 = PostgresDbInvitation.admin("user1");
        var invitationCode3 = PostgresDbInvitation.admin("user2");
        var invitationCode4 = PostgresDbInvitation.admin("user2");
        var invitationCode5 = PostgresDbInvitation.admin("user2");
        var invitationCode6 = PostgresDbInvitation.admin("user3");

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

    public static List<PostgresDbInvitation> dummies2() {
        var invitationCode1 = PostgresDbInvitation.admin("owner22", "value22");
        var invitationCode2 = PostgresDbInvitation.admin("owner22", "abc");
        var invitationCode3 = PostgresDbInvitation.admin("owner22", "value44");
        var invitationCode4 = PostgresDbInvitation.admin("owner11", "value222");
        var invitationCode5 = PostgresDbInvitation.admin("owner11", "value111");
        var invitationCode6 = PostgresDbInvitation.admin("owner33", "value123", "invited1");
        var invitationCode7 = PostgresDbInvitation.admin("owner34", "value234", "invited2");
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
    public InvitationId invitationCodeId() {
        return new InvitationId(this.id);
    }

    @JsonIgnore
    @Transient
    public Invitation invitationCode() {
        return new Invitation(
                this.invitationCodeId(),
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
                this.invitationCodeId(),
                this.owner,
                getResponseInvitationCodeAuthoritiesAsField(this.authorities),
                this.value,
                this.invited
        );
    }
}
