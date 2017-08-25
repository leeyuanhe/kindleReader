package kindle.repository;

import kindle.pojo.User;
import org.springframework.data.mongodb.repository.MongoRepository;

/**
 * @description user 接口操作类
 * @author hely
 * @date 2017-08-24
 * @param
 */
public interface UserRepository extends MongoRepository<User,String> {

    User findByUsername(String username);

    User findByEmail(String email);

    User findUserByUsernameOrEmail(String usename, String email);
}
