package io.tech1.framework.foundation.domain.factories.unique;

import lombok.experimental.UtilityClass;

@UtilityClass
public class UniqueValueFactories {

    public static StringUniqueValueFactory stringUniqueValueFactory() {
        return new StringUniqueValueFactory();
    }

    public static IntegerUniqueValueFactory integerUniqueValueFactory() {
        return new IntegerUniqueValueFactory();
    }
}
