package kindle.repository;

import kindle.pojo.Remember;
import org.springframework.data.mongodb.repository.MongoRepository;

/**
 * @description 记住我功能接口
 * @author hely
 * @date 2017-09-01
 * @param
 */
public interface RememberRepository extends MongoRepository<Remember,String> {



}
