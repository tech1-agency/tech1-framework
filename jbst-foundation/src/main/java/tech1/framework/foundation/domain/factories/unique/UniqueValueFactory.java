package tech1.framework.foundation.domain.factories.unique;

@FunctionalInterface
public interface UniqueValueFactory<T> {
    T createValue();
}
