package tech1.framework.foundation.domain.factories.unique;

import tech1.framework.foundation.utilities.random.RandomUtility;

public class IntegerUniqueValueFactory implements UniqueValueFactory<Integer> {

    @Override
    public Integer createValue() {
        return RandomUtility.randomIntegerGreaterThanZero();
    }
}
