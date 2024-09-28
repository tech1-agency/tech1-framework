package tech1.framework.iam.configurations;

import com.mongodb.ConnectionString;
import com.mongodb.MongoClientSettings;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.autoconfigure.jdbc.DataSourceTransactionManagerAutoConfiguration;
import org.springframework.boot.autoconfigure.orm.jpa.HibernateJpaAutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.MongoDatabaseFactory;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.SimpleMongoClientDatabaseFactory;
import org.springframework.data.mongodb.core.convert.DefaultDbRefResolver;
import org.springframework.data.mongodb.core.convert.MappingMongoConverter;
import org.springframework.data.mongodb.core.mapping.MongoMappingContext;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;
import tech1.framework.foundation.domain.properties.ApplicationFrameworkProperties;
import tech1.framework.iam.repositories.mongodb.MongoInvitationCodesRepository;
import tech1.framework.iam.repositories.mongodb.MongoUsersRepository;
import tech1.framework.iam.repositories.mongodb.MongoUsersSessionsRepository;
import tech1.framework.iam.repositories.mongodb.Tech1MongoRepositories;

@Configuration
@EntityScan({
        "tech1.framework.iam.domain.mongo"
})
@EnableMongoRepositories(
        basePackages = "tech1.framework.iam.repositories",
        mongoTemplateRef = "tech1MongoTemplate"
)
@EnableAutoConfiguration(exclude = {
        DataSourceAutoConfiguration.class,
        DataSourceTransactionManagerAutoConfiguration.class,
        HibernateJpaAutoConfiguration.class
})
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class ApplicationMongoRepositories {

    // Properties
    private final ApplicationFrameworkProperties applicationFrameworkProperties;

    @Bean
    public Tech1MongoRepositories tech1MongoRepositories(
            MongoInvitationCodesRepository mongoInvitationCodesRepository,
            MongoUsersRepository mongoUsersRepository,
            MongoUsersSessionsRepository userSessionRepository
    ) {
        return new Tech1MongoRepositories(
                mongoInvitationCodesRepository,
                mongoUsersRepository,
                userSessionRepository
        );
    }

    @Bean
    public MongoClient tech1MongoClient() {
        var mongodb = this.applicationFrameworkProperties.getMongodbSecurityJwtConfigs().getMongodb();
        var connectionString = new ConnectionString(mongodb.connectionString());
        var mongoClientSettings = MongoClientSettings.builder()
                .applyConnectionString(connectionString)
                .build();
        return MongoClients.create(mongoClientSettings);
    }

    @Bean
    public MongoDatabaseFactory tech1MongoDatabaseFactory() {
        return new SimpleMongoClientDatabaseFactory(
                this.tech1MongoClient(),
                this.applicationFrameworkProperties.getMongodbSecurityJwtConfigs().getMongodb().getDatabase()
        );
    }

    @Bean
    public MongoTemplate tech1MongoTemplate() {
        var dbRefResolver = new DefaultDbRefResolver(this.tech1MongoDatabaseFactory());
        var mongoConverter = new MappingMongoConverter(dbRefResolver, new MongoMappingContext());
        return new MongoTemplate(
                this.tech1MongoDatabaseFactory(),
                mongoConverter
        );
    }
}
