package io.tech1.framework.iam.mongo.configs;

import org.junit.jupiter.api.extension.BeforeAllCallback;
import org.junit.jupiter.api.extension.ExtensionContext;

public class MongoBeforeAllCallback implements BeforeAllCallback {

    @Override
    public void beforeAll(ExtensionContext context) {
        MongoContainerV506.container.start();
        this.setProperties(MongoContainerV506.container);
    }

    private void setProperties(MongoContainerV506 container) {
        System.setProperty("tech1.mongodb-security-jwt-configs.mongodb.host", container.getHost());
        System.setProperty("tech1.mongodb-security-jwt-configs.mongodb.port", String.valueOf(container.getFirstMappedPort()));
        System.setProperty("tech1.mongodb-security-jwt-configs.mongodb.database", "test");
    }

}
