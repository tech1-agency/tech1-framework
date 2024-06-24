package io.tech1.framework.domain.factories.unique;

@FunctionalInterface
public interface UniqueValueFactory<T> {
    T createValue();
}
