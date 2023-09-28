package io.tech1.framework.domain.tests.constants;

import io.tech1.framework.domain.base.Email;
import io.tech1.framework.domain.constants.DomainConstants;
import lombok.experimental.UtilityClass;

@UtilityClass
public class TestsEmailsConstants {
    public static final Email TECH1 = Email.of("tech1@" + DomainConstants.TECH1);
}
