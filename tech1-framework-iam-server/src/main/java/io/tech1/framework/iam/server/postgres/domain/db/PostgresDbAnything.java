package io.tech1.framework.iam.server.postgres.domain.db;

import io.tech1.framework.foundation.domain.base.Username;
import io.tech1.framework.foundation.domain.converters.columns.PostgresUsernameConverter;
import jakarta.persistence.*;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

import static io.tech1.framework.foundation.utilities.random.RandomUtility.randomStringLetterOrNumbersOnly;
import static io.tech1.framework.iam.server.postgres.constants.TablesConstants.ANYTHING;

// Lombok
@NoArgsConstructor
@Getter
@EqualsAndHashCode
@ToString
// JPA
@Entity
@Table(name = ANYTHING)
public class PostgresDbAnything {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(length = 36, nullable = false, updatable = false)
    private String id;

    @Convert(converter = PostgresUsernameConverter.class)
    @Column(nullable = false)
    private Username username;

    @Column
    private String value;

    public PostgresDbAnything(Username username) {
        this.username = username;
        this.value = randomStringLetterOrNumbersOnly(20);
    }
}
