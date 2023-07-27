package io.tech1.framework.b2b.postgres.security.jwt.domain.db;

import io.tech1.framework.b2b.postgres.security.jwt.converters.columns.PostgresSimpleGrantedAuthoritiesConverter;
import io.tech1.framework.b2b.postgres.security.jwt.converters.columns.PostgresUsernameConverter;
import io.tech1.framework.domain.base.Username;
import lombok.*;
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
    @GeneratedValue
    private Long id;

    @Convert(converter = PostgresUsernameConverter.class)
    @Column
    private Username owner;

    @Convert(converter = PostgresSimpleGrantedAuthoritiesConverter.class)
    @Column
    private List<SimpleGrantedAuthority> authorities;

    @Column
    private String value;

    @Convert(converter = PostgresUsernameConverter.class)
    @Column
    private Username invited;

    public PostgresDbInvitationCode(Username owner, List<SimpleGrantedAuthority> authorities) {
        this.owner = owner;
        this.authorities = authorities;
        this.value = randomStringLetterOrNumbersOnly(DEFAULT_INVITATION_CODE_LENGTH);
    }
}
