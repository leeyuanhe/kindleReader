package kindle.controller;


import com.sun.deploy.net.URLEncoder;
import kindle.pojo.User;
import kindle.pojo.result.ExceptionMsg;
import kindle.pojo.result.Response;
import kindle.pojo.result.ResponseData;
import kindle.repository.UserRepository;
import kindle.utils.CommonUtils;
import kindle.utils.Constants;
import kindle.utils.PasswordUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;
import java.util.Date;

@RestController
@RequestMapping("user")
public class UserController extends BaseController {

    @Autowired
    UserRepository userRepository;

    @RequestMapping(value = "/login", method = RequestMethod.POST)
    public ModelAndView login(@ModelAttribute User user,HttpServletResponse response) {
        ModelAndView mv = new ModelAndView();
        try {
            User byUsernameOrEmail = userRepository.findUserByUsernameOrEmail(user.getUsername(), user.getUsername());
            Cookie cookie = new Cookie("username", URLEncoder.encode(user.getUsername(), "UTF-8"));

            if (CommonUtils.isEmpty(byUsernameOrEmail)) {
                mv.addObject("message", ExceptionMsg.LoginNameNotExist.getMsg());
                mv.setViewName("redirect:/");
                return mv;
            } else if (!PasswordUtils.getMD5(user.getPassword() + byUsernameOrEmail.getSalt())
                    .equals(byUsernameOrEmail.getPassword())) {
                mv.addObject("message", ExceptionMsg.LoginNameOrPassWordError.getMsg());
                mv.setViewName("redirect:/");
                cookie.setMaxAge(Constants.COOKIE_USERNAME_TIMEOUT);
                cookie.setPath("/");
                response.addCookie(cookie);
                return mv;
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        mv.setViewName("pages/admin3/index");
        return mv;

    }


    @RequestMapping(value = "/register", method = RequestMethod.POST)
    public Response registerUser(@ModelAttribute User user, HttpServletResponse response) {

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
        user.setCdate(new Date());
        userRepository.save(user);
        return result();
    }
}
