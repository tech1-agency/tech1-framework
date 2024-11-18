package tech1.framework.foundation.feigns.clients;

import tech1.framework.foundation.domain.base.ServerName;
import tech1.framework.foundation.feigns.definitions.SpringBootClientFeign;

class TestSpringBootClient extends BaseSpringBootClient {

    public TestSpringBootClient(SpringBootClientFeign springBootClientFeign) {
        super(springBootClientFeign);
    }

    @Override
    public ServerName getServerName() {
        return ServerName.testsHardcoded();
    }
}
