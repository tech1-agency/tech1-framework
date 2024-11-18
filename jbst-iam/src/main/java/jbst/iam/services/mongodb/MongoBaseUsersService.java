package jbst.iam.services.mongodb;

import jbst.iam.repositories.mongodb.MongoUsersRepository;
import jbst.iam.services.abstracts.AbstractBaseUsersService;
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
