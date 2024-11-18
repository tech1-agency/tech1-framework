package jbst.foundation.domain.maven;

import jbst.foundation.domain.base.Version;
import jbst.foundation.domain.constants.StringConstants;

public record MavenDetails(
        String groupId,
        String artifactId,
        Version version
) {

    public static MavenDetails undefined() {
        return new MavenDetails(StringConstants.UNDEFINED, StringConstants.UNDEFINED, Version.undefined());
    }

    public static MavenDetails dash() {
        return new MavenDetails(StringConstants.DASH, StringConstants.DASH, Version.dash());
    }

    public static MavenDetails testsHardcoded() {
        return new MavenDetails("tech1.framework", "tech1-server", Version.testsHardcoded());
    }
}
