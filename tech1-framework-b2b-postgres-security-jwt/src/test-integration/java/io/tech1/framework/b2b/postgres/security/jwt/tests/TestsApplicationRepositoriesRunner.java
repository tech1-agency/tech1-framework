package io.tech1.framework.b2b.postgres.security.jwt.tests;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

@SuppressWarnings("resource")
@Testcontainers
@EntityScan({
        "io.tech1.framework.b2b.postgres.security.jwt.domain.db"
})
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public abstract class TestsApplicationRepositoriesRunner {
    public static final String POSTGRES_VERSION = "postgres:15.3";
    public static final String POSTGRES_DATABASE_NAME = "integration-tests-db";
    public static final String POSTGRES_USERNAME = "sa";
    public static final String POSTGRES_PASSWORD = "sa";
     public static final String POSTGRES_INIT_SQL = "database/v001_tech1_framework_schema.sql";

    @Container
    private static final PostgreSQLContainer<?> container = new PostgreSQLContainer<>(POSTGRES_VERSION)
            .withDatabaseName(POSTGRES_DATABASE_NAME)
            .withUsername(POSTGRES_USERNAME)
            .withPassword(POSTGRES_PASSWORD)
            .withInitScript(POSTGRES_INIT_SQL);

    @DynamicPropertySource
    static void setProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.url", container::getJdbcUrl);
        registry.add("spring.datasource.username", container::getUsername);
        registry.add("spring.datasource.password", container::getPassword);
        registry.add("spring.jpa.show-sql", () -> true);
        registry.add("spring.jpa.properties.hibernate.dialect", () -> "org.hibernate.dialect.PostgreSQLDialect");
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
        this.getJpaRepository().deleteAll();
    }

    public abstract JpaRepository<?, String> getJpaRepository();
}
