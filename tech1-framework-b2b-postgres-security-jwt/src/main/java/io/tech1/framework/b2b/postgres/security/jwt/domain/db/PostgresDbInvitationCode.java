package io.tech1.framework.b2b.postgres.security.jwt.domain.db;

import com.fasterxml.jackson.annotation.JsonIgnore;
import io.tech1.framework.b2b.base.security.jwt.domain.db.AnyDbInvitationCode;
import io.tech1.framework.b2b.base.security.jwt.domain.identifiers.InvitationCodeId;
import io.tech1.framework.b2b.postgres.security.jwt.converters.columns.PostgresSimpleGrantedAuthoritiesConverter;
import io.tech1.framework.b2b.postgres.security.jwt.converters.columns.PostgresUsernameConverter;
import io.tech1.framework.domain.base.Username;
import lombok.*;
import org.hibernate.annotations.GenericGenerator;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import javax.persistence.*;
import java.util.List;

import static io.tech1.framework.b2b.base.security.jwt.constants.SecurityJwtConstants.DEFAULT_INVITATION_CODE_LENGTH;
import static io.tech1.framework.b2b.postgres.security.jwt.constants.PostgreTablesConstants.INVITATION_CODES;
import static io.tech1.framework.domain.utilities.random.RandomUtility.randomStringLetterOrNumbersOnly;

// Lombok
@NoArgsConstructor
@Getter
@Setter
@EqualsAndHashCode
@ToString
// JPA
@Entity
@Table(name = INVITATION_CODES)
public class PostgresDbInvitationCode {
    @Id
    @GenericGenerator(name = "uuid2", strategy = "uuid2")
    @GeneratedValue(strategy = GenerationType.IDENTITY, generator = "uuid2")
    @Column(length = 36, nullable = false, updatable = false)
    private String id;

    @Convert(converter = PostgresUsernameConverter.class)
    @Column(nullable = false, updatable = false)
    private Username owner;

    @Convert(converter = PostgresSimpleGrantedAuthoritiesConverter.class)
    @Column(length = 1024, nullable = false)
    private List<SimpleGrantedAuthority> authorities;

    @Column(nullable = false)
    private String value;

    @Convert(converter = PostgresUsernameConverter.class)
    @Column
    private Username invited;

    public PostgresDbInvitationCode(Username owner, List<SimpleGrantedAuthority> authorities) {
        this.owner = owner;
        this.authorities = authorities;
        this.value = randomStringLetterOrNumbersOnly(DEFAULT_INVITATION_CODE_LENGTH);
    }

    @JsonIgnore
    @Transient
    public AnyDbInvitationCode anyDbInvitationCode() {
        return new AnyDbInvitationCode(
                new InvitationCodeId(this.id),
                this.owner,
                this.authorities,
                this.value,
                this.invited
        );
    }
}
