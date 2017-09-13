package kindle.service;

import kindle.mapper.UserMapper;
import kindle.pojo.User;
import kindle.pojo.UserExample;
import kindle.utils.CommonUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserService {

    @Autowired
    UserMapper userMapper;
    /**
     * @description 用户名和邮箱查重
     * @author hely
     * @date 2017-09-13
     * @param
     */
    public String checkNameAndEmail(User user){

        if (!CommonUtils.isEmpty(user.getUserName())){
            UserExample userExample = new UserExample();
            UserExample.Criteria usercri = userExample.createCriteria();
            usercri.andUserNameEqualTo(user.getUserName());
            List<User> users = userMapper.selectByExample(userExample);
            if (!CommonUtils.isEmpty(users)){
                return "002";
            }
        }

        if (!CommonUtils.isEmpty(user.getEmail())){
            UserExample userExample = new UserExample();
            UserExample.Criteria usercri = userExample.createCriteria();
            usercri.andEmailEqualTo(user.getEmail());
            List<User> users = userMapper.selectByExample(userExample);
            if (!CommonUtils.isEmpty(users)){
                return "001";
            }
        }
        return "";
    }

}
