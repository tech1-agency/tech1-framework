package tech1.framework.iam.services.mongodb;

import tech1.framework.iam.repositories.mongodb.MongoUsersRepository;
import tech1.framework.iam.services.abstracts.AbstractBaseUsersService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class MongoBaseUsersService extends AbstractBaseUsersService {

    @Autowired
    public MongoBaseUsersService(
            MongoUsersRepository usersRepository,
            BCryptPasswordEncoder bCryptPasswordEncoder
    ) {
        super(
                usersRepository,
                bCryptPasswordEncoder
        );
    }
}
