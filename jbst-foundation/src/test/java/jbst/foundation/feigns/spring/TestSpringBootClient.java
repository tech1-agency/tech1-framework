package jbst.foundation.feigns.spring;

import jbst.foundation.domain.base.ServerName;

class TestSpringBootClient extends BaseSpringBootClient {

    public TestSpringBootClient(SpringBootDefinition definition) {
        super(definition);
    }

    @Override
    public ServerName getServerName() {
        return ServerName.hardcoded();
    }
}
