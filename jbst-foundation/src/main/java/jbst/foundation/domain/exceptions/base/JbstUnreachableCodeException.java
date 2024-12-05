package jbst.foundation.domain.exceptions.base;

import static jbst.foundation.utilities.exceptions.ExceptionsMessagesUtility.contactDevelopmentTeam;

public class JbstUnreachableCodeException extends IllegalArgumentException {

    public JbstUnreachableCodeException() {
        super(contactDevelopmentTeam("Unreachable code"));
    }
}
