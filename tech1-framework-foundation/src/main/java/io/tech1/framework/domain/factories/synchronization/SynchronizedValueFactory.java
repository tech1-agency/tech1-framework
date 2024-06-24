package io.tech1.framework.domain.factories.synchronization;

@FunctionalInterface
public interface SynchronizedValueFactory<T> {
    T createValue();
}
