package io.tech1.framework.b2b.postgres.security.jwt.domain.db;

import io.tech1.framework.b2b.postgres.security.jwt.converters.columns.PostgresPasswordConverter;
import io.tech1.framework.b2b.postgres.security.jwt.converters.columns.PostgresUsernameConverter;
import io.tech1.framework.domain.base.Password;
import io.tech1.framework.domain.base.Username;
import io.tech1.framework.domain.enums.Status;
import io.tech1.framework.domain.utilities.random.RandomUtility;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

import javax.persistence.*;

import static io.tech1.framework.b2b.postgres.security.jwt.constants.PostgreTablesConstants.USERS;
import static io.tech1.framework.domain.utilities.random.RandomUtility.randomPassword;
import static io.tech1.framework.domain.utilities.random.RandomUtility.randomStringLetterOrNumbersOnly;

// Lombok
@NoArgsConstructor
@Getter
@EqualsAndHashCode
@ToString
// JPA
@Entity
@Table(name = USERS)
public class PostgresDbUser {
    @Id
    @GeneratedValue
    private Long id;

    @Convert(converter = PostgresUsernameConverter.class)
    @Column
    private Username username;

    @Convert(converter = PostgresPasswordConverter.class)
    @Column
    private Password password;

    @Column
    private String name;

    @Column
    private Status status;

    public PostgresDbUser(Username username) {
        this.username = username;
        this.password = randomPassword();
        this.name = randomStringLetterOrNumbersOnly(30);
        this.status = RandomUtility.randomEnum(Status.class);
    }
}
