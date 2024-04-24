package io.tech1.framework.b2b.postgres.security.jwt.domain.superclasses;

import jakarta.persistence.*;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.id.uuid.UuidGenerator;
import org.springframework.data.domain.Persistable;
import org.springframework.lang.Nullable;

import static java.util.Objects.isNull;

@MappedSuperclass
public abstract class PostgresDbAbstractPersistable0 implements Persistable<String> {
    @Id
    @GenericGenerator(name = "uuid2", type = UuidGenerator.class)
    @GeneratedValue(strategy = GenerationType.IDENTITY, generator = "uuid2")
    @Column(length = 36, nullable = false, updatable = false)
    protected String id;

    protected PostgresDbAbstractPersistable0() {
        // ignored
    }

    @Nullable
    @Override
    public String getId() {
        return this.id;
    }

    // DATAJPA-622
    @Transient
    @Override
    public boolean isNew() {
        return isNull(this.getId());
    }
}
