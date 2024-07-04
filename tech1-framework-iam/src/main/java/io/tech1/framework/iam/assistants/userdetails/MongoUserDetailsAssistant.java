package io.tech1.framework.iam.assistants.userdetails;

import io.tech1.framework.iam.repositories.mongo.MongoUsersRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@SuppressWarnings("SpringJavaInjectionPointsAutowiringInspection")
@Slf4j
@Service
public class MongoUserDetailsAssistant extends AbstractJwtUserDetailsService {

    @Autowired
    public MongoUserDetailsAssistant(
            MongoUsersRepository usersRepository
    ) {
        super(
                usersRepository
        );
    }
}
