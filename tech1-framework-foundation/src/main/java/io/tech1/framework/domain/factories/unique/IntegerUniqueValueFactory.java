package io.tech1.framework.domain.factories.unique;

import io.tech1.framework.foundation.utilities.random.RandomUtility;

public class IntegerUniqueValueFactory implements UniqueValueFactory<Integer> {

    @Override
    public Integer createValue() {
        return RandomUtility.randomIntegerGreaterThanZero();
    }
}
