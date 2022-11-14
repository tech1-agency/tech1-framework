package io.tech1.framework.utilities.feigns.clients;

import io.tech1.framework.utilities.feigns.definitions.SpringBootClientFeign;

public class TestSpringBootClient extends BaseSpringBootClient {

    public TestSpringBootClient(SpringBootClientFeign springBootClientFeign) {
        super(springBootClientFeign);
    }

    @Override
    public String getServerName() {
        return "test-server";
    }
}
