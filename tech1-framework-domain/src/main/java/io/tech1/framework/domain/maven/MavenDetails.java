package io.tech1.framework.domain.maven;

import io.tech1.framework.domain.base.Version;

import static io.tech1.framework.domain.constants.StringConstants.UNDEFINED;

public record MavenDetails(
        String groupId,
        String artifactId,
        Version version
) {

    public static MavenDetails undefined() {
        return new MavenDetails(UNDEFINED, UNDEFINED, Version.undefined());
    }

    public static MavenDetails testsHardcoded() {
        return new MavenDetails("io.tech1.framework", "tech1-server", Version.testsHardcoded());
    }
}
