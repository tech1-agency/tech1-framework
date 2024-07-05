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
        System.setProperty("spring.data.mongodb.uri", container.getReplicaSetUrl());
    }

}
