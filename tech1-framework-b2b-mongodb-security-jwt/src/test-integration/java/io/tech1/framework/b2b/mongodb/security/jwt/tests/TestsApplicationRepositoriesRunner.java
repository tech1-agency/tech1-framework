package io.tech1.framework.b2b.mongodb.security.jwt.tests;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.MongoDBContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

@Testcontainers
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public abstract class TestsApplicationRepositoriesRunner {
    public static final String MONGO_DB_VERSION = "mongo:5.0.6";
    public static final int MONGO_DB_PORT = 27017;

    @Container
    private static final MongoDBContainer container = new MongoDBContainer(MONGO_DB_VERSION).withExposedPorts(MONGO_DB_PORT);

    @DynamicPropertySource
    static void setProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.data.mongodb.uri", container::getReplicaSetUrl);
    }

    @BeforeAll
    public static void beforeAll() {
        container.start();
    }

    @AfterAll
    public static void afterAll() {
        container.stop();
    }

    @AfterEach
    void afterEach() {
        this.getMongoRepository().deleteAll();
    }

    public abstract MongoRepository<?, String> getMongoRepository();
}
