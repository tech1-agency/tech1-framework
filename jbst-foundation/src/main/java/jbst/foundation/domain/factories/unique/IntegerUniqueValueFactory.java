package jbst.foundation.domain.factories.unique;

import jbst.foundation.utilities.random.RandomUtility;

public class IntegerUniqueValueFactory implements UniqueValueFactory<Integer> {

    @Override
    public Integer createValue() {
        return RandomUtility.randomIntegerGreaterThanZero();
    }
}
