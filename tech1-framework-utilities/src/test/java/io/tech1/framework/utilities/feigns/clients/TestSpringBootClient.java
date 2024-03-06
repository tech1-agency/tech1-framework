package io.tech1.framework.utilities.feigns.clients;

import io.tech1.framework.domain.base.ServerName;
import io.tech1.framework.utilities.feigns.definitions.SpringBootClientFeign;

class TestSpringBootClient extends BaseSpringBootClient {

    public TestSpringBootClient(SpringBootClientFeign springBootClientFeign) {
        super(springBootClientFeign);
    }

    @Override
    public ServerName getServerName() {
        return new ServerName("test-server");
    }
}
