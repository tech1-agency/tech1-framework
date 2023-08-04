package io.tech1.framework.b2b.mongodb.security.jwt.tests;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.repository.CrudRepository;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.MongoDBContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.util.List;

@Testcontainers
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public abstract class TestsAbstractMongoRepositoriesRunner {
    public static final String MONGO_DB_VERSION = "mongo:5.0.6";
    public static final int MONGO_DB_PORT = 27017;

    @Container
    private static final MongoDBContainer CONTAINER = new MongoDBContainer(MONGO_DB_VERSION)
            .withExposedPorts(MONGO_DB_PORT);

    @DynamicPropertySource
    static void setProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.data.mongodb.uri", CONTAINER::getReplicaSetUrl);
    }

    @BeforeAll
    public static void beforeAll() {
        CONTAINER.start();
    }

    @AfterAll
    public static void afterAll() {
        CONTAINER.stop();
    }

    @AfterEach
    void afterEach() {
        this.getMongoRepositories().forEach(CrudRepository::deleteAll);
    }

    public abstract List<MongoRepository<?, String>> getMongoRepositories();
}
