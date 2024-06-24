package io.tech1.framework.b2b.postgres.security.jwt.domain.superclasses;

import io.tech1.framework.b2b.postgres.security.jwt.converters.columns.PostgresUsernameConverter;
import io.tech1.framework.domain.base.Username;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import jakarta.persistence.*;

import static io.tech1.framework.foundation.utilities.time.TimestampUtility.getCurrentTimestamp;

@Getter
@Setter
@NoArgsConstructor
@MappedSuperclass
public abstract class PostgresDbAbstractPersistable2 extends PostgresDbAbstractPersistable0 {

    @Convert(converter = PostgresUsernameConverter.class)
    @Column(name = "created_by", nullable = false)
    protected Username createdBy;

    @Column(name = "created_at", nullable = false)
    protected long createdAt;

    protected PostgresDbAbstractPersistable2(Username createdBy) {
        this.createdBy = createdBy;
        this.createdAt = getCurrentTimestamp();
    }
}
