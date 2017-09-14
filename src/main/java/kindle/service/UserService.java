package kindle.service;

import kindle.mapper.RememberMapper;
import kindle.mapper.UserMapper;
import kindle.pojo.Remember;
import kindle.pojo.User;
import kindle.pojo.UserExample;
import kindle.utils.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.ModelAttribute;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.Date;
import java.util.List;

@Service
public class UserService {

    @Autowired
    UserMapper userMapper;
    @Autowired
    RememberMapper rememberMapper;
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

    /**
     * @description 记住我
     * @author hely
     * @date 2017-09-14
     * @param
     */
    public void rememberMe(HttpServletRequest request, HttpServletResponse response,
                           String rememberStr, User dbUser) throws UnsupportedEncodingException {
        String cookieValue = CookieUtils.getCookieValue(request, Constants.COOKIE_NAME);
        Remember remember ;
        if (Constants.REMEMBER_FLAG.equals(rememberStr)) {
            String invariable_series = PasswordUtils.getMD5(RandomUtils.generateMixString(32));
            String usertoken = PasswordUtils.getMD5(RandomUtils.generateMixString(64));
            String key = URLEncoder.encode(invariable_series+Constants.SEPRETOR_FLAG+dbUser.getUserName()+Constants
                    .SEPRETOR_FLAG+usertoken, "utf-8");

            remember = rememberMapper.selectOne(new Remember(null,null, dbUser.getId()));
            if (CommonUtils.isEmpty(remember)){
                remember = new Remember(invariable_series,usertoken,dbUser.getId());
                remember.setCreateTime(new Date());
                rememberMapper.insert(remember);
            }else{
                remember.setInvariableSeries(invariable_series);
                remember.setToken(usertoken);
                remember.setUserId(dbUser.getId());
                remember.setModifyTime(new Date());
                rememberMapper.updateByPrimaryKey(remember);
            }
            CookieUtils.addCookie(response,Constants.COOKIE_NAME,key,Constants
                    .COOKIE_USERNAME_TIMEOUT);
        }else if (!Constants.REMEMBER_FLAG.equals(rememberStr) && !CommonUtils.isEmpty(cookieValue)){
            String[] split = cookieValue.split(Constants.SEPRETOR_FLAG);
            String cookieUuid = split[0];
            rememberMapper.delete(new Remember(cookieUuid,null,null));
            CookieUtils.removeCookie(response, Constants.COOKIE_NAME);
        }
    }

    /**
     * @description 根据用户名或密码查询
     * @author hely
     * @date 2017-09-14
     * @param
     */
    public User selectByNameOrEmail(User user){

        UserExample userExample = new UserExample();
        UserExample.Criteria criteria = userExample.createCriteria();
        criteria.andUserNameEqualTo(user.getUserName());
        userExample.or().andEmailEqualTo(user.getUserName());;
        List<User> users = userMapper.selectByExample(userExample);
        if (!CommonUtils.isEmpty(users)){
            return users.get(0);
        }
        return null;
    }

}
