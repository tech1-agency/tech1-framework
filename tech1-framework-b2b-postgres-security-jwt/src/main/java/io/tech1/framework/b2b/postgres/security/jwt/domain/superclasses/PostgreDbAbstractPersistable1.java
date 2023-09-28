package io.tech1.framework.b2b.postgres.security.jwt.domain.superclasses;

import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.GenericGenerator;
import org.springframework.data.domain.Persistable;
import org.springframework.lang.Nullable;

import javax.persistence.*;

import static io.tech1.framework.domain.utilities.time.TimestampUtility.getCurrentTimestamp;
import static java.util.Objects.isNull;

@MappedSuperclass
public abstract class PostgreDbAbstractPersistable1 implements Persistable<String> {
    @Id
    @GenericGenerator(name = "uuid2", strategy = "uuid2")
    @GeneratedValue(strategy = GenerationType.IDENTITY, generator = "uuid2")
    @Column(length = 36, nullable = false, updatable = false)
    protected String id;

    @Getter
    @Setter
    @Column(name = "created_at", nullable = false)
    protected long createdAt;

    @Getter
    @Setter
    @Column(name = "updated_at", nullable = false)
    protected long updatedAt;

    protected PostgreDbAbstractPersistable1() {
        var currentTimestamp = getCurrentTimestamp();
        this.createdAt = currentTimestamp;
        this.updatedAt = currentTimestamp;
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

    protected void updated() {
        this.updatedAt = getCurrentTimestamp();
    }
}
