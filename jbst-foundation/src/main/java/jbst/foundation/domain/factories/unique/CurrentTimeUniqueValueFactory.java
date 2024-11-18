package jbst.foundation.domain.factories.unique;

import java.util.concurrent.atomic.AtomicInteger;

public final class CurrentTimeUniqueValueFactory implements UniqueValueFactory<Integer> {
    private final AtomicInteger unique;

    public CurrentTimeUniqueValueFactory() {
        this.unique = new AtomicInteger(0);
    }

    // WARNING: create problems in "Tuesday, January 19, 2038 3:14:07 AM" == Integer.MAX_VALUE
    @Override
    public Integer createValue() {
        return this.unique.updateAndGet(prevUnique -> {
            int newUnique = (int) (System.currentTimeMillis() / 1000);
            if (newUnique <= prevUnique) {
                newUnique = prevUnique + 1;
            }
            return newUnique;
        });
    }
}
