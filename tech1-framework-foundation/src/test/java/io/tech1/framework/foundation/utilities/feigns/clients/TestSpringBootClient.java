package io.tech1.framework.foundation.utilities.feigns.clients;

import io.tech1.framework.foundation.domain.base.ServerName;
import io.tech1.framework.foundation.utilities.feigns.definitions.SpringBootClientFeign;

class TestSpringBootClient extends BaseSpringBootClient {

    public TestSpringBootClient(SpringBootClientFeign springBootClientFeign) {
        super(springBootClientFeign);
    }

    @Override
    public ServerName getServerName() {
        return ServerName.testsHardcoded();
    }
}
