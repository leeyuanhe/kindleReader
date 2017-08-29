package kindle.controller;


import kindle.pojo.User;
import kindle.pojo.result.ExceptionMsg;
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
        User byUsernameOrEmail = userRepository.findUserByUsernameOrEmail(user.getUsername(), user.getUsername());
        Cookie cookie = new Cookie("username",user.getUsername());
        ModelAndView mv = new ModelAndView();
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
        mv.setViewName("pages/admin3/index");
        return mv;
    }


    @RequestMapping(value = "/register", method = RequestMethod.POST)
    public ModelAndView registerUser(@ModelAttribute User user, HttpServletResponse response) {

        User byUserName = userRepository.findByUsername(user.getUsername());
        ModelAndView mv = new ModelAndView();
        if (!CommonUtils.isEmpty(byUserName)) {
            mv.addObject("error", ExceptionMsg.UserNameUsed);
            mv.setViewName("login_soft");
            return mv;
        }

        User byEmail = userRepository.findByEmail(user.getEmail());

        if (!CommonUtils.isEmpty(byEmail)) {
            mv.addObject("error", ExceptionMsg.EmailUsed);
            mv.setViewName("login_soft");
            return mv;
        }
        String salt = PasswordUtils.generateRandomSalt();
        user.setPassword(PasswordUtils.getMD5(user.getPassword() + salt));
        user.setSalt(salt);
        user.setCdate(new Date());
        userRepository.save(user);
        mv.setViewName("index");
        return mv;
    }
}
