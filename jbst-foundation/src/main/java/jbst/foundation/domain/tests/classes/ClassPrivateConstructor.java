package jbst.foundation.domain.tests.classes;

import lombok.Getter;
import lombok.Setter;

public class ClassPrivateConstructor {

    private ClassPrivateConstructor() {}

    @Getter
    @Setter
    private String string;
}
