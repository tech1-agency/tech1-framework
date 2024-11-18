package jbst.foundation.domain.factories.synchronization;

@FunctionalInterface
public interface SynchronizedValueFactory<T> {
    T createValue();
}
