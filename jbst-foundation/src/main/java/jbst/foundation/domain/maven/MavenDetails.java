package jbst.foundation.domain.maven;

import jbst.foundation.domain.base.Version;
import jbst.foundation.domain.constants.JbstConstants;

public record MavenDetails(
        String groupId,
        String artifactId,
        Version version
) {

    public static MavenDetails hardcoded() {
        return new MavenDetails("jbst", "jbst", Version.hardcoded());
    }

    public static MavenDetails undefined() {
        return new MavenDetails(JbstConstants.Strings.UNDEFINED, JbstConstants.Strings.UNDEFINED, Version.undefined());
    }

    public static MavenDetails dash() {
        return new MavenDetails(JbstConstants.Symbols.DASH, JbstConstants.Symbols.DASH, Version.dash());
    }
}
