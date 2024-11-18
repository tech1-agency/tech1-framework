package jbst.foundation.domain.factories.unique;

import jbst.foundation.utilities.random.RandomUtility;

public class StringUniqueValueFactory implements UniqueValueFactory<String> {

    @Override
    public String createValue() {
        return RandomUtility.randomString();
    }
}
