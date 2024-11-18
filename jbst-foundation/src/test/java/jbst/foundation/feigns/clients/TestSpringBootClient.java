package jbst.foundation.feigns.clients;

import jbst.foundation.domain.base.ServerName;
import jbst.foundation.feigns.definitions.SpringBootClientFeign;

class TestSpringBootClient extends BaseSpringBootClient {

    public TestSpringBootClient(SpringBootClientFeign springBootClientFeign) {
        super(springBootClientFeign);
    }

    @Override
    public ServerName getServerName() {
        return ServerName.hardcoded();
    }
}
