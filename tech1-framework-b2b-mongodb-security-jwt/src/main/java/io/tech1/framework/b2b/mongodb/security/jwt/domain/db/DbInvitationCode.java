package io.tech1.framework.b2b.mongodb.security.jwt.domain.db;

import io.tech1.framework.domain.base.Username;
import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.List;

import static io.tech1.framework.b2b.base.security.jwt.constants.SecurityJwtConstants.DEFAULT_INVITATION_CODE_LENGTH;
import static io.tech1.framework.domain.utilities.random.RandomUtility.randomStringLetterOrNumbersOnly;

// Lombok
@NoArgsConstructor
@Getter
@Setter
@EqualsAndHashCode
@ToString
// Mongodb
@Document(collection = "tech1_invitation_codes")
public class DbInvitationCode {
    @Id
    private String id;

    private Username owner;
    private List<SimpleGrantedAuthority> authorities;
    private String value;

    private Username invited;

    public DbInvitationCode(
            Username owner,
            List<SimpleGrantedAuthority> authorities
    ) {
        this.owner = owner;
        this.authorities = authorities;
        this.value = randomStringLetterOrNumbersOnly(DEFAULT_INVITATION_CODE_LENGTH);
    }
}
