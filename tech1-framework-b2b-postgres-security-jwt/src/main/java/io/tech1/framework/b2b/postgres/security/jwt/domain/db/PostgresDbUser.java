package io.tech1.framework.b2b.postgres.security.jwt.domain.db;

import io.tech1.framework.b2b.postgres.security.jwt.converters.columns.PostgresUsernameConverter;
import io.tech1.framework.domain.base.Username;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

import javax.persistence.*;

import static io.tech1.framework.b2b.postgres.security.jwt.constants.PostgreTablesConstants.USERS;

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
    private String id;

    @Convert(converter = PostgresUsernameConverter.class)
    @Column
    private Username username;
}
