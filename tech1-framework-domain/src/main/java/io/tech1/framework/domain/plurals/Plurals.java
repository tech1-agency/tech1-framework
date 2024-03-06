package io.tech1.framework.domain.plurals;

import lombok.EqualsAndHashCode;
import lombok.ToString;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

// Lombok
@EqualsAndHashCode
@ToString
public abstract class Plurals<T extends Plurable<ID>, ID> {
    protected final List<T> values;
    protected final Map<ID, T> mappedValues;

    protected Plurals(List<T> values) {
        this.values = values;
        this.mappedValues = values.stream().collect(Collectors.toMap(Plurable::getId, entry -> entry));
    }

    public final T getOne(ID id) {
        return this.mappedValues.get(id);
    }

    public final List<ID> getIds() {
        return this.values.stream().map(Plurable::getId).collect(Collectors.toList());
    }

    public final Set<ID> getUniqueIds() {
        return this.mappedValues.keySet();
    }
}
