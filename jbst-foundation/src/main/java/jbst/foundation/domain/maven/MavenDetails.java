package jbst.foundation.domain.maven;

import jbst.foundation.domain.base.Version;
import jbst.foundation.domain.constants.JbstConstants;
import jbst.foundation.domain.constants.StringConstants;

public record MavenDetails(
        String groupId,
        String artifactId,
        Version version
) {

    public static MavenDetails hardcoded() {
        return new MavenDetails("jbst", "jbst", Version.hardcoded());
    }

    public static MavenDetails undefined() {
        return new MavenDetails(StringConstants.UNDEFINED, StringConstants.UNDEFINED, Version.undefined());
    }

    public static MavenDetails dash() {
        return new MavenDetails(JbstConstants.Symbols.DASH, JbstConstants.Symbols.DASH, Version.dash());
    }
}
