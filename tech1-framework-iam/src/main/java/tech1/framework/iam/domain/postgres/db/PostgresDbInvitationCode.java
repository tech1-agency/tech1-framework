package tech1.framework.iam.domain.postgres.db;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import tech1.framework.foundation.domain.base.Username;
import tech1.framework.foundation.domain.converters.columns.PostgresUsernameConverter;
import tech1.framework.iam.converters.columns.PostgresSetOfSimpleGrantedAuthoritiesConverter;
import tech1.framework.iam.domain.db.InvitationCode;
import tech1.framework.iam.domain.dto.responses.ResponseInvitationCode;
import tech1.framework.iam.domain.identifiers.InvitationCodeId;
import tech1.framework.iam.domain.postgres.superclasses.PostgresDbAbstractPersistable0;

import java.util.Set;

import static tech1.framework.foundation.utilities.random.RandomUtility.randomStringLetterOrNumbersOnly;
import static tech1.framework.iam.utilities.SpringAuthoritiesUtility.getResponseInvitationCodeAuthoritiesAsField;
import static tech1.framework.iam.utilities.SpringAuthoritiesUtility.getSimpleGrantedAuthorities;

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
