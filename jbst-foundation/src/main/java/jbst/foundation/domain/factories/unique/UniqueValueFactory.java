package jbst.foundation.domain.factories.unique;

@FunctionalInterface
public interface UniqueValueFactory<T> {
    T createValue();
}
