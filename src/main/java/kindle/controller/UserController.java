package kindle.controller;


import kindle.pojo.Remember;
import kindle.pojo.User;
import kindle.pojo.result.ExceptionMsg;
import kindle.pojo.result.Response;
import kindle.repository.RememberRepository;
import kindle.repository.UserRepository;
import kindle.utils.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.net.URLEncoder;
import java.util.Date;

@RestController
@RequestMapping("user")
public class UserController extends BaseController {

    @Autowired
    UserRepository userRepository;
    @Autowired
    RememberRepository rememberRepository;


    @RequestMapping(value = "/login", method = RequestMethod.POST)
    public Response login(@ModelAttribute User user, HttpServletRequest request, HttpServletResponse response) {
        String remember = request.getParameter("remember");
        try {
            User byUsernameOrEmail = userRepository.findUserByUsernameOrEmail(user.getUsername(), user.getUsername());

            if (CommonUtils.isEmpty(byUsernameOrEmail)) {
                return result(ExceptionMsg.LoginNameNotExist);
            } else if (!PasswordUtils.getMD5(user.getPassword() + byUsernameOrEmail.getSalt())
                    .equals(byUsernameOrEmail.getPassword())) {
                return  result(ExceptionMsg.LoginNameOrPassWordError);
            }
            request.getSession().setAttribute(Constants.SESSSION_USER_KEY, user);
            //记住我功能cookie处理
            String cookieValue = CookieUtils.getCookieValue(request, Constants.COOKIE_NAME);

            if (Constants.REMEMBER_FLAG.equals(remember)) {
                String invariable_series = PasswordUtils.getMD5(RandomUtils.generateMixString(32));
                String token = PasswordUtils.getMD5(RandomUtils.generateMixString(64));
                String key = URLEncoder.encode(invariable_series+Constants.SEPRETOR_FLAG+user.getUsername()+Constants
                        .SEPRETOR_FLAG+token, "utf-8");

                rememberRepository.save(new Remember(invariable_series,token,user));
                CookieUtils.addCookie(response,Constants.COOKIE_NAME,key,Constants
                        .COOKIE_USERNAME_TIMEOUT);
            }else if (!Constants.REMEMBER_FLAG.equals(remember) && !CommonUtils.isEmpty(cookieValue)){
                String[] split = cookieValue.split(Constants.SEPRETOR_FLAG);
                String cookieUuid = split[0];
                rememberRepository.deleteByUuid(cookieUuid);
                CookieUtils.removeCookie(response, Constants.COOKIE_NAME);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return result();
    }


    @RequestMapping(value = "/register", method = RequestMethod.POST)
    public Response registerUser(User user, HttpServletRequest request) {

        User byUserName = userRepository.findByUsername(user.getUsername());
        if (!CommonUtils.isEmpty(byUserName)) {
            return result(ExceptionMsg.UserNameUsed);
        }

        User byEmail = userRepository.findByEmail(user.getEmail());

        if (!CommonUtils.isEmpty(byEmail)) {
            return result(ExceptionMsg.EmailUsed);
        }
        String salt = PasswordUtils.generateRandomSalt();
        user.setPassword(PasswordUtils.getMD5(user.getPassword() + salt));
        user.setSalt(salt);
        user.setIpAddress(CommonUtils.getRemoteAddr(request));
        user.setCdate(new Date());
        userRepository.save(user);
        return result();
    }
}
