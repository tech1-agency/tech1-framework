package io.tech1.framework.domain.factories.unique;

import io.tech1.framework.foundation.utilities.random.RandomUtility;

public class StringUniqueValueFactory implements UniqueValueFactory<String> {

    @Override
    public String createValue() {
        return RandomUtility.randomString();
    }
}
