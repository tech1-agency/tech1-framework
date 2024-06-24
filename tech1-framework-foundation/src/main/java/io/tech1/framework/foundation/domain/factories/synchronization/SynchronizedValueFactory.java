package io.tech1.framework.foundation.domain.factories.synchronization;

@FunctionalInterface
public interface SynchronizedValueFactory<T> {
    T createValue();
}
