package io.tech1.framework.b2b.postgres.server.domain.db;

import io.tech1.framework.b2b.postgres.security.jwt.converters.columns.PostgresUsernameConverter;
import io.tech1.framework.domain.base.Username;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

import javax.persistence.*;

import static io.tech1.framework.b2b.postgres.server.constants.TablesConstants.ANYTHING;
import static io.tech1.framework.domain.utilities.random.RandomUtility.randomStringLetterOrNumbersOnly;

// Lombok
@NoArgsConstructor
@Getter
@EqualsAndHashCode
@ToString
// JPA
@Entity
@Table(name = ANYTHING)
public class PostgresDbAnything {
    // WARNING: add sequence generation
    @Id
    private String id;

    @Convert(converter = PostgresUsernameConverter.class)
    @Column
    private Username username;

    @Column
    private String value;

    public PostgresDbAnything(Username username) {
        this.id = randomStringLetterOrNumbersOnly(10);
        this.username = username;
        this.value = randomStringLetterOrNumbersOnly(20);
    }
}
