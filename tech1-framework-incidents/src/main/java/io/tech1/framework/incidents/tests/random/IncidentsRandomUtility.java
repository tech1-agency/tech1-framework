package io.tech1.framework.incidents.tests.random;

import io.tech1.framework.domain.base.Username;
import io.tech1.framework.incidents.domain.Incident;
import io.tech1.framework.incidents.domain.system.IncidentSystemResetServerCompleted;
import io.tech1.framework.incidents.domain.system.IncidentSystemResetServerStarted;
import io.tech1.framework.incidents.domain.throwable.IncidentThrowable;
import lombok.experimental.UtilityClass;

import static io.tech1.framework.domain.utilities.random.RandomUtility.randomString;

@UtilityClass
public class IncidentsRandomUtility {

    public static IncidentSystemResetServerStarted randomIncidentSystemResetServerStarted() {
        return new IncidentSystemResetServerStarted(
                Username.of("tech1")
        );
    }

    public static IncidentSystemResetServerCompleted randomIncidentSystemResetServerCompleted() {
        return new IncidentSystemResetServerCompleted(
                Username.of("tech1")
        );
    }

    public static Incident randomIncident() {
        var incident = new Incident();
        incident.add(randomString(), randomString());
        incident.add(randomString(), randomString());
        incident.add(randomString(), randomString());
        return incident;
    }

    public static IncidentThrowable randomThrowableIncident() {
        return IncidentThrowable.of(
                new Throwable(randomString())
        );
    }
}
