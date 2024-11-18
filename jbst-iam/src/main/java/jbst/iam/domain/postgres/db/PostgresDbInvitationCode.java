package jbst.iam.domain.postgres.db;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jbst.iam.converters.columns.PostgresSetOfSimpleGrantedAuthoritiesConverter;
import jbst.iam.domain.db.InvitationCode;
import jbst.iam.domain.dto.responses.ResponseInvitationCode;
import jbst.iam.domain.identifiers.InvitationCodeId;
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
@Table(name = PostgresDbInvitationCode.PG_TABLE_NAME)
public class PostgresDbInvitationCode extends PostgresDbAbstractPersistable0 {
    public static final String PG_TABLE_NAME = "tech1_invitation_codes";

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

    public PostgresDbInvitationCode(Username owner, Set<SimpleGrantedAuthority> authorities) {
        this.owner = owner;
        this.authorities = authorities;
        this.value = randomStringLetterOrNumbersOnly(InvitationCode.DEFAULT_INVITATION_CODE_LENGTH);
    }

    public PostgresDbInvitationCode(InvitationCode invitationCode) {
        this.id = invitationCode.id().value();
        this.owner = invitationCode.owner();
        this.authorities = invitationCode.authorities();
        this.value = invitationCode.value();
        this.invited = invitationCode.invited();
    }

    public static PostgresDbInvitationCode admin(String owner) {
        return new PostgresDbInvitationCode(Username.of(owner), getSimpleGrantedAuthorities("admin"));
    }

    public static PostgresDbInvitationCode admin(String owner, String value) {
        var invitationCode = admin(owner);
        invitationCode.setValue(value);
        return invitationCode;
    }

    public static PostgresDbInvitationCode admin(String owner, String value, String invited) {
        var invitationCode = admin(owner, value);
        invitationCode.setInvited(Username.of(invited));
        return invitationCode;
    }

    public static List<PostgresDbInvitationCode> dummies1() {
        var invitationCode1 = PostgresDbInvitationCode.admin("user1");
        var invitationCode2 = PostgresDbInvitationCode.admin("user1");
        var invitationCode3 = PostgresDbInvitationCode.admin("user2");
        var invitationCode4 = PostgresDbInvitationCode.admin("user2");
        var invitationCode5 = PostgresDbInvitationCode.admin("user2");
        var invitationCode6 = PostgresDbInvitationCode.admin("user3");

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

    public static List<PostgresDbInvitationCode> dummies2() {
        var invitationCode1 = PostgresDbInvitationCode.admin("owner22", "value22");
        var invitationCode2 = PostgresDbInvitationCode.admin("owner22", "abc");
        var invitationCode3 = PostgresDbInvitationCode.admin("owner22", "value44");
        var invitationCode4 = PostgresDbInvitationCode.admin("owner11", "value222");
        var invitationCode5 = PostgresDbInvitationCode.admin("owner11", "value111");
        var invitationCode6 = PostgresDbInvitationCode.admin("owner33", "value123", "invited1");
        var invitationCode7 = PostgresDbInvitationCode.admin("owner34", "value234", "invited2");
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
