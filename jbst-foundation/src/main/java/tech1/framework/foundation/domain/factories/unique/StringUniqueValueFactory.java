package tech1.framework.foundation.domain.factories.unique;

import tech1.framework.foundation.utilities.random.RandomUtility;

public class StringUniqueValueFactory implements UniqueValueFactory<String> {

    @Override
    public String createValue() {
        return RandomUtility.randomString();
    }
}
